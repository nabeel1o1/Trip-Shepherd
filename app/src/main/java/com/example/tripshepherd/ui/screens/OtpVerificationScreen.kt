package com.example.tripshepherd.ui.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripshepherd.R
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText
import com.example.tripshepherd.ui.components.OtpInputField
import com.example.tripshepherd.utils.SignInState
import com.example.tripshepherd.viewmodel.OtpVerificationViewModel
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun OtpVerificationScreen(
    currentActivity: Activity,
    verificationId: String,
    resendingToken: ForceResendingToken,
    phoneNo: String,
    onOtpVerificationSuccess: () -> Unit,
    onBackPress : () -> Unit
) {

    val viewModel: OtpVerificationViewModel = hiltViewModel()

    val verificationState by viewModel.signInState.collectAsState()

    var mVerificationId by remember { mutableStateOf(verificationId) }

    var resendToken by remember { mutableStateOf(resendingToken) }

    when (verificationState) {

        is SignInState.VerificationCompleted -> {
            LaunchedEffect(Unit) {
                onOtpVerificationSuccess()
                viewModel.clearNavigationState()
            }
        }

        is SignInState.Error -> {
            val errorMessage = (verificationState as SignInState.Error).message
            Log.d("TAG_TS", "SignUpScreen: $errorMessage")
            LaunchedEffect(Unit) {
                Toast.makeText(currentActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        is SignInState.CodeSent -> {
            LaunchedEffect(Unit) {
                mVerificationId = (verificationState as SignInState.CodeSent).verificationId
                resendToken = (verificationState as SignInState.CodeSent).token
                Log.d("TAG_TS", "verificationId: $mVerificationId")
                viewModel.clearNavigationState()
            }
        }

        else -> {}
    }

    var otpValue by remember { mutableStateOf("") }
    var isOtpFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var counter by remember { mutableIntStateOf(12) }
    var showResendOption by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = showResendOption) {
        while (counter > 0) {
            delay(1000L)
            counter -= 1
        }
        showResendOption = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = {
            onBackPress()
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back button"
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            HeadingWithTitle(
                title = "OTP Verification",
                description = "Enter the verification code we just sent to\n$phoneNo"
            )

            Spacer(modifier = Modifier.height(50.dp))

            OtpInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
                    .focusRequester(focusRequester),
                otpText = otpValue,
                onOtpModified = { value, otpFilled ->
                    otpValue = value
                    isOtpFilled = otpFilled
                    if (otpFilled) {
                        keyboardController?.hide()
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OtpTimeLimit(counter)

            Spacer(modifier = Modifier.height(30.dp))

            if (showResendOption)
                ResendCodeText(onResendClick = {
                    viewModel.resendOTP(currentActivity, phoneNo, resendToken)
                    counter = 12
                    showResendOption = false
                })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isOtpFilled)
                        viewModel.verifyOtp(mVerificationId, otpValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = verificationState != SignInState.Loading,
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                if (verificationState == SignInState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else
                    Text(text = "Continue", color = Color.White, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        LogoWithText()
    }
}

@Composable
private fun OtpTimeLimit(counter: Int) {
    Text(
        text = if (counter > 0)
            "00:${String.format(Locale.getDefault(), "%02d", counter)} Sec" else "",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ResendCodeText(onResendClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("Didn’t get the code? ")

        pushStringAnnotation(tag = "RESEND", annotation = "resend")
        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
            append("Click to resend")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            color = Color.Gray,
            fontSize = 14.sp
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "RESEND", start = offset, end = offset)
                .firstOrNull()?.let {
                    onResendClick()
                }
        }
    )
}