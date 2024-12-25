package com.example.tripshepherd.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripshepherd.R
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText
import com.example.tripshepherd.ui.components.TextFieldWithLabel
import kotlinx.coroutines.launch

@Composable
fun UserEmailScreen(
    onNextClick : () -> Unit
) {
    var userEmail by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var contenxt = LocalContext.current

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

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            HeadingWithTitle(title = "What’s your email address?",
                description = "Make sure your email is correct.")

            Spacer(modifier = Modifier.height(50.dp))

            TextFieldWithLabel(value = userEmail, labelTxt = "Email address", onValueChange = {
                userEmail = it
            })

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    if (userEmail.isNotEmpty())
                    {
                        if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                            onNextClick()
                        else
                        {
                            scope.launch {
                                Toast.makeText(contenxt, "Enter a valid email", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Next", color = Color.White, fontSize = 16.sp)
            }

        Spacer(modifier = Modifier.weight(1f))

        LogoWithText()
    }
}
}