package com.example.tripshepherd.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.tripshepherd.R
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText
import com.example.tripshepherd.ui.components.OtpInputField
import com.example.tripshepherd.ui.components.TextFieldWithLabel

@Composable
fun OtpVerificationScreen(modifier: Modifier = Modifier, phoneNo: String = "92 332 525 8828") {

    val context = LocalContext.current
    val otpValue = remember { mutableStateOf("") }
    val isOtpFilled = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back button"
            )
        }

        Column(modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(20.dp))

            HeadingWithTitle(title = "OTP Verification",
                description = "Enter the verification code we just sent to\n+$phoneNo")

            Spacer(modifier = Modifier.height(50.dp))

            OtpInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
                    .focusRequester(focusRequester),
                otpText = otpValue.value,
                onOtpModified = { value, otpFilled ->
                    otpValue.value = value
                    isOtpFilled.value = otpFilled
                    if (otpFilled) {
                        keyboardController?.hide()
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OtpTimeLimit()

            Spacer(modifier = Modifier.height(30.dp))

            ResendCodeText(onResendClick = { /* Handle resend code */ })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Handle Continue */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Continue", color = Color.White, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        LogoWithText()
    }
}

@Composable
fun OtpTimeLimit() {
    Text(text = "00:12 Sec",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth())
}

@Composable
fun ResendCodeText(onResendClick: () -> Unit) {
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