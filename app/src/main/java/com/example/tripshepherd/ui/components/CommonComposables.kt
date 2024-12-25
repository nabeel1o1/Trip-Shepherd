package com.example.tripshepherd.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.tripshepherd.R

@Composable
fun LogoWithText() {
    Box(modifier = Modifier
        .fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_ts_logo),
            contentDescription = "Trip Shepherd Logo"
        )
        Text(
            text = "Powered by Tripshepherd",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HeadingWithTitle(title: String, description: String, textAlign : TextAlign? = null) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = description,
        fontSize = 14.sp,
        color = Color.Gray,
        textAlign = textAlign
    )
}

@Composable
fun TextFieldWithLabel(
    value: String,
    labelTxt: String,
    onValueChange: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (label, textField) = createRefs()

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .constrainAs(textField){}
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(9.dp))
                .padding(horizontal = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            )
        )

        Text(
            text = labelTxt,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .constrainAs(label) {
                    start.linkTo(textField.start, margin = 16.dp)
                    top.linkTo(textField.top)
                    bottom.linkTo(textField.top)
                }
                .background(Color.White)
                .padding(horizontal = 5.dp)
        )
    }
}