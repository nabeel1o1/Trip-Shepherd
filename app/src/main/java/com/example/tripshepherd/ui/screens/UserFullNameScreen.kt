package com.example.tripshepherd.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun UserFullNameScreen(
    onNextClick: () -> Unit,
    onBackPress: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = { onBackPress() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back button"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            HeadingWithTitle(
                title = "What’s your full name?",
                description = "It will be displayed on your profile. If you need to, you can change it later."
            )

            Spacer(modifier = Modifier.height(50.dp))

            TextFieldWithLabel(value = fullName, labelTxt = "Full name", onValueChange = {
                fullName = it
            })

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    if (fullName.isNotEmpty())
                        onNextClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Next", color = Color.White, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        LogoWithText()
    }
}