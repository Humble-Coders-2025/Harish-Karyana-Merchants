package com.example.harishkaryanamerchants.screens.registerScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harishkaryanamerchants.R
import com.example.harishkaryanamerchants.ui.theme.darkPurpleColor

@Composable
fun RegisterScreenUI() {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Get screen height to calculate proper spacing
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topImageHeight = screenHeight / 2 // Image will take top half of screen

    Box(modifier = Modifier.fillMaxSize()
        .background(Color.White)) {
        // Top image that covers the entire width and top half of screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(horizontal = 20.dp)
        ) {
        Image(
            painter = painterResource(id = R.drawable.registerbackground),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(topImageHeight.dp),
            contentScale = ContentScale.FillBounds
        )

        // Top bar with back button that overlays on the image
        IconButton(
            onClick = { /* Handle back navigation */ },
            modifier = Modifier.padding(top = 20.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Content column with proper spacing from the top

            // Add spacing to position content below the image
            Spacer(modifier = Modifier.height(56.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {

                // Full Name field
                Text(
                    text = "Full Name",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
                    color = Color.Black
                )
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
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

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number field
                Text(
                    text = "Your Phone Number",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    placeholder = {
                        Text(
                            text = "Your Phone Number",
                            color = Color.Gray,
                            fontSize = 14.sp
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

                Spacer(modifier = Modifier.weight(1f))

                // Button
                Button(
                    onClick = { /* Handle Send OTP action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF580768)
                    )
                ) {
                    Text(
                        text = "Send OTP",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 16.sp,
                        color = darkPurpleColor
                    )
                    Text(
                        text = "   Login",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = darkPurpleColor,
                        modifier = Modifier.clickable { /* Handle login navigation */ }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}