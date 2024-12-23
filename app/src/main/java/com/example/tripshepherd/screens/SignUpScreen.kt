package com.example.tripshepherd.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripshepherd.R
import com.example.tripshepherd.ui.components.Country
import com.example.tripshepherd.ui.components.CountryPickerBottomSheet
import com.example.tripshepherd.ui.components.HeadingWithTitle
import com.example.tripshepherd.ui.components.LogoWithText

@Composable
fun SignUpScreen() {

    val mobileNumber = remember { mutableStateOf(TextFieldValue("")) }

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

            HeadingWithTitle(title = "Let's get started",
                description = "Enter your mobile number to continue")

            Spacer(modifier = Modifier.height(50.dp))

            MobileNumberInputField()

            Spacer(modifier = Modifier.height(50.dp))

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
                onClick = { /* Handle Google Sign-In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
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
fun MobileNumberInputField() {
    val mobileNumber = remember { mutableStateOf("") }
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
                    .padding(end = 8.dp)
                    .clickable {
                        showBottomSheet = true
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pakistan_flag),
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
                    text = "+92",
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
                value = mobileNumber.value,
                onValueChange = { mobileNumber.value = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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
                listOfCountry = Country.getAllCountries(),
                onDismissRequest = {
                    showBottomSheet = false
                },
                onItemClicked = {},
            )
        }
    }
}