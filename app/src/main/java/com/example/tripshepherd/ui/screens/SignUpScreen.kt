package com.example.tripshepherd.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripshepherd.MainActivity
import com.example.tripshepherd.R
import com.example.tripshepherd.utils.PhoneNoValidator
import com.example.tripshepherd.utils.Country
import com.example.tripshepherd.utils.countryList
import com.example.tripshepherd.utils.getFlags
import com.example.tripshepherd.ui.components.CountryPickerBottomSheet
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText
import com.example.tripshepherd.utils.SignInState
import com.example.tripshepherd.viewmodel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    currentActivity: MainActivity,
    onNavigateToOtpVerification: (
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken,
        phoneNo: String
    ) -> Unit,
    onNavigateToAccountCreated: (userInfo: FirebaseUser) -> Unit,
    onBackPress: () -> Unit
) {

    val context = LocalContext.current

    val viewModel: SignUpViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    var mobileNumber by remember { mutableStateOf("") }

    val validatePhoneNumber = remember(currentActivity) {
        PhoneNoValidator(context = currentActivity)
    }

    var selectedCountry by remember {
        mutableStateOf(Country(name = "Pakistan", dialCode = "+92", code = "pk"))
    }

    val signInState by viewModel.signInState.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    viewModel.signInWithGoogle(idToken)
                }
            } catch (e: ApiException) {
                scope.launch {
                    Log.d("TAG_TS", "Google Sign-In failed: ${e.localizedMessage}")
                    Toast.makeText(
                        context,
                        "Google Sign-In failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )

    when (signInState) {

        is SignInState.Error -> {
            val errorMessage = (signInState as SignInState.Error).message
            Log.d("TAG_TS", "SignUpScreen: $errorMessage")
            LaunchedEffect(Unit) {
                Toast.makeText(currentActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        is SignInState.CodeSent -> {
            LaunchedEffect(Unit) {
                val verificationId = (signInState as SignInState.CodeSent).verificationId
                val phoneNo = (signInState as SignInState.CodeSent).phoneNo
                val resendToken = (signInState as SignInState.CodeSent).token
                Log.d("TAG_TS", "verificationId: $verificationId")
                onNavigateToOtpVerification(verificationId, resendToken, phoneNo)
            }
        }

        is SignInState.GoogleSignInSuccess -> {
            LaunchedEffect(Unit) {
                val userInfo = (signInState as SignInState.GoogleSignInSuccess).user
                onNavigateToAccountCreated(userInfo!!)
            }
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = { onBackPress()}) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back button"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            HeadingWithTitle(
                title = "Let's get started",
                description = "Enter your mobile number to continue"
            )

            Spacer(modifier = Modifier.height(50.dp))

            MobileNumberInputField(mobileNumber, selectedCountry, { country ->
                selectedCountry = country
            }, {
                mobileNumber = it
            })

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    if (mobileNumber.isNotEmpty()) {
                        if (validatePhoneNumber(
                                countryCode = selectedCountry.dialCode,
                                number = mobileNumber
                            )
                        ) {
                            viewModel.startPhoneVerification(
                                phoneNumber = selectedCountry.dialCode + mobileNumber,
                                currentActivity
                            )
                        } else {
                            scope.launch {
                                Toast.makeText(
                                    currentActivity,
                                    "Enter a valid phone number",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } else {
                        scope.launch {
                            Toast.makeText(currentActivity, "Enter phone number", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = signInState != SignInState.Loading,
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                if (signInState == SignInState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else
                    Text(text = "Continue", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Text(
                    text = "or",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val signInIntent = viewModel.getGoogleSignInIntent()
                    googleSignInLauncher.launch(signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                enabled = signInState != SignInState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFEFEF)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continue with Google",
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        LogoWithText()
    }
}

@Composable
fun MobileNumberInputField(
    mobileNumber: String, selectedCountry: Country,
    onSelectedCountry: (country: Country) -> Unit,
    onValueChange: (String) -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(9.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .clickable {
                        showBottomSheet = true
                    }
            ) {
                Image(
                    painter = painterResource(id = getFlags(selectedCountry.code.lowercase())),
                    contentDescription = "Country Flag",
                    modifier = Modifier
                        .size(24.dp)
                        .shadow(5.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown_arrow),
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.size(10.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedCountry.dialCode,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                VerticalDivider(
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
            }

            TextField(
                value = mobileNumber,
                onValueChange = { onValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
        }
        Text(
            text = "Mobile Number",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 19.dp)
                .offset(y = (-13).dp)
                .background(Color.White)
                .width(100.dp)
        )

        if (showBottomSheet) {
            CountryPickerBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                listOfCountry = countryList(LocalContext.current),
                onDismissRequest = {
                    showBottomSheet = false
                },
                onItemClicked = {
                    onSelectedCountry(it)
                    showBottomSheet = false
                },
            )
        }
    }
}
