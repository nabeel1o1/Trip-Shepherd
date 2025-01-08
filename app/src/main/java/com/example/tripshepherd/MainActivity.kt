package com.example.tripshepherd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripshepherd.ui.screens.AccountCreatedScreen
import com.example.tripshepherd.ui.screens.OtpVerificationScreen
import com.example.tripshepherd.ui.screens.SignUpScreen
import com.example.tripshepherd.ui.screens.UserEmailScreen
import com.example.tripshepherd.ui.screens.UserFullNameScreen
import com.example.tripshepherd.ui.theme.TripshepherdTheme
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripshepherdTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp)
                ) {
                    App(this@MainActivity)
                }
            }
        }
    }

    @Composable
    fun App(currentActivity: MainActivity) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "signup") {
            composable("signup") {
                SignUpScreen(currentActivity, { verificationId, resendToken, phoneNo ->
                    this@MainActivity.resendToken = resendToken
                    navController.navigate("otpVerification/$verificationId/$phoneNo")
                }, { userInfo ->
                    navController.navigate("accountCreated")
                }, {
                    finish()
                })
            }
            composable("otpVerification/{verificationId}/{phoneNo}") {
                navArgument(name = "verificationId") {
                    type = NavType.StringType
                }
                navArgument(name = "phoneNo") {
                    type = NavType.StringType
                }
                val verificationId = it.arguments!!.getString("verificationId")
                val phoneNo = it.arguments!!.getString("phoneNo")
                OtpVerificationScreen(
                    this@MainActivity, verificationId!!,
                    resendToken!!, phoneNo!!, {
                        navController.navigate("userFullName")
                    },
                    {
                        navController.popBackStack()
                    })
            }
            composable("userFullName") {
                UserFullNameScreen({
                    navController.navigate("userEmail")
                }, {
                    navController.popBackStack()
                })
            }
            composable("userEmail") {
                UserEmailScreen({
                    navController.navigate("accountCreated")
                }, {
                    navController.popBackStack()
                })
            }
            composable("accountCreated") {
                AccountCreatedScreen()
            }
        }
    }
}

