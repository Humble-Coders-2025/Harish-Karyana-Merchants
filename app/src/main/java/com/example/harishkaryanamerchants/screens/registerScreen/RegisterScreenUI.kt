package com.example.harishkaryanamerchants.screens.registerScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harishkaryanamerchants.R


@Composable
fun RegisterScreenUI() {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.registerbackground),
            contentDescription = null
        )

        IconButton(
            onClick = { /* Handle back navigation */ },
            modifier = Modifier.padding(top = 35.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Row(
            modifier = Modifier.padding(top = 100.dp)
        ){
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(400.dp))
            Text(
                text = "Full Name",
                fontSize = 15.sp,
                modifier = Modifier.align(alignment = Alignment.Start).padding(start = 6.dp),
                color = Color.Black
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Your Name",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Color(0xFFE5E5E5),
                    focusedContainerColor = Color(0xFFE5E5E5)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Your Phone Number",
                fontSize = 15.sp,
                modifier = Modifier.align(alignment = Alignment.Start).padding(start = 6.dp),
                color = Color.Black
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Row(){
                        Text(
                            text = "Your Phone Number",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Icon(
                            modifier = Modifier
                                .padding(start = 150.dp)
                                .clickable {  }, // Toggle visibility on click
                            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                            contentDescription = null,
                        )
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Color(0xFFE5E5E5),
                    focusedContainerColor = Color(0xFFE5E5E5)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle Send OTP action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xCC0580768),
                )
            ) {
                Text(
                    text = "Send OTP",
                    fontSize = 20.sp,
                    color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(){
                Text(
                    text = "Already have an account?",
                    fontSize = 16.sp,
                    color = Color(0xFF580768)
                )
                Text(
                    text = "   Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF580768)
                )
            }
        }
    }
}