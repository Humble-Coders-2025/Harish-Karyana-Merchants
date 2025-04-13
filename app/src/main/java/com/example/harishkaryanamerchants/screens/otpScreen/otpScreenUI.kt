package com.example.harishkaryanamerchants.screens.otpScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.harishkaryanamerchants.R
import com.example.harishkaryanamerchants.navigation.UserData
import com.example.harishkaryanamerchants.viewmodels.AuthViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log

@SuppressLint("InvalidColorHexValue")
@Composable
fun OTPScreenUI(
    userData: UserData? = null,
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onRegistrationComplete: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State for timer
    var resendTimerSeconds by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    // Collect states from ViewModel
    val otpState by viewModel.otpState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()

    // Track if OTP has been sent
    var otpSent by remember { mutableStateOf(false) }

    // Error message state
    var errorMessage by remember { mutableStateOf("") }

    // Get screen height to calculate proper spacing
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topImageHeight = screenHeight / 2 // Image will take top half of screen

    // Try to initialize Firebase on this screen to ensure it's available
    LaunchedEffect(Unit) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
                Log.d("FirebaseAuth", "Firebase initialized in OTPScreenUI")
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Failed to initialize Firebase in OTPScreenUI: ${e.message}")
            errorMessage = "Failed to initialize Firebase. Please restart the app."
        }
    }

    // Effect to start the timer for resending OTP
    LaunchedEffect(otpSent) {
        if (otpSent) {
            while (resendTimerSeconds > 0) {
                delay(1000)
                resendTimerSeconds--
            }
            canResend = true
        }
    }

    // Effect to handle OTP and registration states
    LaunchedEffect(otpState, registrationState) {
        Log.d("OTPScreen", "OTP State: $otpState")
        Log.d("OTPScreen", "Registration State: $registrationState")

        when (otpState) {
            is AuthViewModel.OtpState.Error -> {
                errorMessage = (otpState as AuthViewModel.OtpState.Error).message
                Log.e("OTPScreen", "OTP Error: $errorMessage")
            }
            is AuthViewModel.OtpState.OtpSent -> {
                otpSent = true
                errorMessage = ""
                Log.d("OTPScreen", "OTP sent successfully")
            }
            is AuthViewModel.OtpState.Verified -> {
                // OTP verification was successful
                // The API call will happen automatically
                errorMessage = ""
                Log.d("OTPScreen", "OTP verified successfully")
            }
            else -> { /* No action for other states */ }
        }

        when (registrationState) {
            is AuthViewModel.RegistrationState.Success -> {
                // Registration successful, navigate to home screen
                Log.d("OTPScreen", "Registration successful, navigating to home")
                onRegistrationComplete()
                viewModel.resetRegistrationState()
                viewModel.resetOtpState()
            }
            is AuthViewModel.RegistrationState.Error -> {
                errorMessage = (registrationState as AuthViewModel.RegistrationState.Error).message
                Log.e("OTPScreen", "Registration Error: $errorMessage")
            }
            else -> { /* No action for other states */ }
        }
    }

    // Effect to initiate OTP verification when screen loads
    LaunchedEffect(userData) {
        userData?.let {
            Log.d("OTPScreen", "Initiating phone verification for: ${it.phoneNumber}")
            viewModel.initiatePhoneVerification(
                it.firstName,
                it.lastName,
                it.phoneNumber,
                context as androidx.activity.ComponentActivity
            )
        }
    }

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
            onClick = onBackClick,
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

                // Show message if OTP sent successfully
                if (otpSent) {
                    Text(
                        text = "OTP sent to ${userData?.phoneNumber}",
                        fontSize = 14.sp,
                        color = Color(0xFF580768),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Error message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // OTP Input
                OTPInputScreen(
                    onOtpComplete = { otp ->
                        Log.d("OTPScreen", "OTP complete: $otp")
                        viewModel.verifyOtp(otp)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resend OTP button
                Button(
                    onClick = {
                        if (canResend && userData != null) {
                            // Reset timer
                            resendTimerSeconds = 60
                            canResend = false

                            // Resend OTP
                            Log.d("OTPScreen", "Resending OTP")
                            scope.launch {
                                viewModel.initiatePhoneVerification(
                                    userData.firstName,
                                    userData.lastName,
                                    userData.phoneNumber,
                                    context as androidx.activity.ComponentActivity
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF580768)
                    ),
                    enabled = canResend
                ) {
                    Text(
                        text = if (canResend) "Resend OTP" else "Resend OTP in ${resendTimerSeconds}s",
                        fontSize = 15.sp,
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Push the buttons to the bottom

                // Loading indicator
                if (otpState is AuthViewModel.OtpState.Loading || registrationState is AuthViewModel.RegistrationState.Loading) {
                    CircularProgressIndicator(
                        color = Color(0xFF580768),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Register button
                Button(
                    onClick = {
                        // This will be handled by the OTP verification flow
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF580768),
                    ),
                    enabled = otpState !is AuthViewModel.OtpState.Loading && registrationState !is AuthViewModel.RegistrationState.Loading
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
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onLoginClick)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPInputScreen(
    onOtpComplete: (String) -> Unit = {}
) {
    val otpLength = 4 // Number of OTP input boxes
    val otpValues = remember { mutableStateListOf("", "", "", "") }

    // Create focus requesters for each OTP field
    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    // Get the focus manager
    val focusManager = LocalFocusManager.current

    // Check if OTP is complete
    val isOtpComplete = remember(otpValues) {
        otpValues.all { it.isNotEmpty() }
    }

    // Effect to handle OTP completion
    LaunchedEffect(isOtpComplete) {
        if (isOtpComplete) {
            val otp = otpValues.joinToString("")
            Log.d("OTPInputScreen", "OTP completed: $otp")
            onOtpComplete(otp)
        }
    }

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
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                        if (newValue.length <= 1) {
                            // Update the current field
                            otpValues[index] = newValue

                            // Auto-move to next field if a digit was entered and not the last field
                            if (newValue.isNotEmpty() && index < otpLength - 1) {
                                // Focus on the next field
                                focusRequesters[index + 1].requestFocus()
                            }
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
            .onKeyEvent{ keyEvent ->
                if(keyEvent.key == Key.Backspace && value.isEmpty() && index > 0){
                    // Move focus to previous field on backspace if current field is empty
                    focusRequesters[index - 1].requestFocus()
                    otpValues[index - 1] = "" // Clear the previous field
                    true
                } else {
                    false
                }
            }
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xFF580768) else Color.Black,
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