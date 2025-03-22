package com.example.harishkaryanamerchants.screens.otpScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harishkaryanamerchants.R

@Composable
fun OTPScreenUI() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.registerbackground),
            contentDescription = null
        )

        Row(
            modifier = Modifier.padding(top = 100.dp)
        ) {
            Text(
                text = "Register",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(top = 180.dp, start = 30.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.boy_pic),
                contentDescription = null,
                modifier = Modifier.size(360.dp)
            )
        }

        Column {

            OTPInputScreen()

            Button(
                onClick = { /* Handle Send OTP action */ },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Change button background color
                    contentColor = Color(0xCC580768) // Change button text color
                )
            ){
                Text(
                    text = "Resend OTP in timer",
                    fontSize = 15.sp,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xCC0580768),
                )
            ){
                Text(text = "Register",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row() {
                Text(
                    text = "Already have an account?",
                    fontSize = 16.sp,
                    color = Color(0xFF580768),
                    modifier = Modifier.padding(start = 60.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF580768),
                    modifier = Modifier.padding(start = 30.dp)
                )
            }
        }
    }
}




@Composable
fun OTPInputScreen() {
    val otpLength = 4 // Number of OTP input boxes
    val otpValues = remember { mutableStateListOf("", "", "", "") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 520.dp, start = 68.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0 until otpLength).forEach { index ->
            OTPInputField(
                value = otpValues[index],
                onValueChange = { value ->
                    if (value.length <= 1) {
                        otpValues[index] = value
                        if (value.isNotEmpty() && index < otpLength - 1) {
                            // Automatically move focus to next box
                            otpValues[index + 1] = ""
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun OTPInputField(value: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .size(height = 60.dp, width = 50.dp)
            .border(
                width = 1.dp, // Border width
                color = Color.Black, // Border color
                shape = RoundedCornerShape(3.dp) // Rounded corners
            )
            .padding(15.dp)
            .background(Color.Transparent),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center // Center the text horizontally
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}