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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.harishkaryanamerchants.R
import com.example.harishkaryanamerchants.ui.theme.darkPurpleColor
import com.example.harishkaryanamerchants.viewmodels.AuthViewModel

@Composable
fun RegisterScreenUI(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onSendOtpClick: (String, String, String) -> Unit = { _, _, _ -> },
    onLoginClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Error state
    var fullNameError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Get screen height to calculate proper spacing
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topImageHeight = screenHeight / 2 // Image will take top half of screen

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // Top image that covers the entire width and top half of screen
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 20.dp, start = 8.dp)
                    .offset(y = (-topImageHeight).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

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
                    onValueChange = {
                        fullName = it
                        fullNameError = ""
                    },
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
                    isError = fullNameError.isNotEmpty(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (fullNameError.isEmpty()) Color.Transparent else Color.Red,
                        unfocusedBorderColor = if (fullNameError.isEmpty()) Color.Transparent else Color.Red,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color(0xFFE5E5E5),
                        focusedContainerColor = Color(0xFFE5E5E5)
                    )
                )

                if (fullNameError.isNotEmpty()) {
                    Text(
                        text = fullNameError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number field
                Text(
                    text = "Your Phone Number (10 digits)",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it.replace(Regex("[^0-9]"), "")
                        phoneNumberError = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    placeholder = {
                        Text(
                            text = "Your 10-digit phone number",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    isError = phoneNumberError.isNotEmpty(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (phoneNumberError.isEmpty()) Color.Transparent else Color.Red,
                        unfocusedBorderColor = if (phoneNumberError.isEmpty()) Color.Transparent else Color.Red,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color(0xFFE5E5E5),
                        focusedContainerColor = Color(0xFFE5E5E5)
                    )
                )

                if (phoneNumberError.isNotEmpty()) {
                    Text(
                        text = phoneNumberError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                    )
                }

                // Show formatted number preview if the user has entered a phone number
                if (phoneNumber.isNotEmpty()) {
                    val formattedNumber = formatToE164(phoneNumber)
                    Text(
                        text = "Will be sent as: $formattedNumber",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Button
                Button(
                    onClick = {
                        // Validate input
                        var isValid = true

                        if (fullName.isBlank()) {
                            fullNameError = "Please enter your name"
                            isValid = false
                        }

                        if (phoneNumber.isBlank()) {
                            phoneNumberError = "Please enter your phone number"
                            isValid = false
                        } else if (!phoneNumber.matches(Regex("^[0-9]{10}$"))) {
                            phoneNumberError = "Please enter a valid 10-digit phone number"
                            isValid = false
                        }

                        if (isValid) {
                            // Split full name into first and last name
                            val nameParts = fullName.trim().split("\\s+".toRegex(), 2)
                            val firstName = nameParts[0]
                            val lastName = if (nameParts.size > 1) nameParts[1] else ""

                            // Format phone number to E.164 before sending
                            val e164PhoneNumber = formatToE164(phoneNumber)

                            // Navigate to OTP screen
                            onSendOtpClick(firstName, lastName, e164PhoneNumber)
                        }
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
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
                        modifier = Modifier.clickable(onClick = onLoginClick)
                    )
                }
            }
        }
    }
}

// Helper function to format phone number to E.164
private fun formatToE164(phoneNumber: String): String {
    // Remove all non-digit characters
    val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")

    // Ensure we're dealing with an Indian number (10 digits)
    return if (digitsOnly.length == 10) {
        "+91$digitsOnly"  // Add Indian country code with plus
    } else if (digitsOnly.startsWith("91") && digitsOnly.length == 12) {
        "+$digitsOnly"  // Just add plus if it already has country code
    } else {
        "+91$digitsOnly"  // Default to Indian code (might not be correct)
    }
}