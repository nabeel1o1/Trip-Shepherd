package com.example.tripshepherd

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tripshepherd.screens.AccountCreatedScreen
import com.example.tripshepherd.screens.OtpVerificationScreen
import com.example.tripshepherd.screens.SignUpScreen
import com.example.tripshepherd.screens.UserEmailScreen
import com.example.tripshepherd.screens.UserFullNameScreen
import com.example.tripshepherd.ui.theme.TripshepherdTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripshepherdTheme {
                Box(modifier = Modifier
                    .fillMaxSize().padding(top = 30.dp)) {
                    SignUpScreen()
                }
            }
        }
    }
}