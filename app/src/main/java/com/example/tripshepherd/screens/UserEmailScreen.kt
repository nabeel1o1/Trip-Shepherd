package com.example.tripshepherd.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripshepherd.R
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText
import com.example.tripshepherd.ui.components.TextFieldWithLabel

@Composable
fun UserEmailScreen(modifier: Modifier = Modifier) {

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

            TextFieldWithLabel(value = "", labelTxt = "Email address", onValueChange = {

            })

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { /* Handle Continue */ },
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