package com.example.harishkaryanamerchants.screens.otpScreen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harishkaryanamerchants.R

@SuppressLint("InvalidColorHexValue")
@Composable
fun OTPScreenUI() {
    // Get screen height to calculate proper spacing
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topImageHeight = screenHeight / 2 // Image will take top half of screen

    // Full screen background image
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // Top image that covers the entire width and top half of screen
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

        // Content column - position starts AFTER the image height
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = topImageHeight.dp) // This ensures content starts below the image
        ) {
            // OTP content with proper spacing
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp), // Adjusted to match your original 50dp padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier=Modifier.height(10.dp))
                // OTP text label
                Text(
                    text = "Enter OTP",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF580768),
                    modifier = Modifier.padding(vertical = 26.dp)
                )

                // OTP Input
                OTPInputScreen()

                Spacer(modifier = Modifier.height(16.dp))

                // Resend OTP button
                Button(
                    onClick = { /* Handle Send OTP action */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF580768)
                    )
                ) {
                    Text(
                        text = "Resend OTP in timer",
                        fontSize = 15.sp,
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Push the buttons to the bottom

                // Register button
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF580768),
                    )
                ) {
                    Text(
                        text = "Register",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login text
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 16.sp,
                        color = Color(0xFF580768)
                    )

                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF580768),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPInputScreen() {
    val otpLength = 4 // Number of OTP input boxes
    val otpValues = remember { mutableStateListOf("", "", "", "") }

    // Create focus requesters for each OTP field
    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    // Get the focus manager
    val focusManager = LocalFocusManager.current

    // Set initial focus to the first OTP box
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until otpLength) {
            OTPInputField(
                value = otpValues[index],
                onValueChange = { newValue ->
                    // Check if the input is a single digit
                    if (newValue.length <= 1) {
                        // Update the current field
                        otpValues[index] = newValue

                        // Auto-move to next field if a digit was entered and not the last field
                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            // Focus on the next field
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                focusRequester = focusRequesters[index],
                index = index,
                focusRequesters = focusRequesters,
                otpLength = otpLength,
                otpValues = otpValues
            )

            if (index < otpLength - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun OTPInputField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    index: Int,
    focusRequesters: List<FocusRequester>,
    otpLength: Int,
    otpValues: MutableList<String>
) {

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .size(height = 60.dp, width = 50.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .onKeyEvent{
                    keyEvent ->
                if(keyEvent.key == Key.Backspace && value.isEmpty() && index > 0){
                    focusRequesters[index - 1].requestFocus()
                    true
                } else {
                    false
                }
            }
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White.copy(alpha = 0.1f)),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(
            onNext = {
                if (index < otpLength - 1) {
                    focusRequesters[index + 1].requestFocus()
                }
            },
            onPrevious = {
                if (index > 0) {
                    focusRequesters[index - 1].requestFocus()
                }
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                innerTextField()
            }
        }
    )

    LaunchedEffect(value) {
        if(value.isNotEmpty() && index < otpLength - 1){
            focusRequesters[index + 1].requestFocus()
        }
    }
}