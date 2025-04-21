package com.example.harishkaryanamerchants.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harishkaryanamerchants.auth.FirebaseAuthService
import com.example.harishkaryanamerchants.auth.FirebaseAuthService.OtpResult
import com.example.harishkaryanamerchants.auth.FirebaseAuthService.OtpVerificationResult
import com.example.harishkaryanamerchants.network.ApiResponse
import com.example.harishkaryanamerchants.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val firebaseAuthService = FirebaseAuthService()
    private val apiService = ApiService()

    // State for registration process
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    // State for OTP verification
    private val _otpState = MutableStateFlow<OtpState>(OtpState.Idle)
    val otpState: StateFlow<OtpState> = _otpState.asStateFlow()

    // Store user data temporarily
    private var firstName: String = ""
    private var lastName: String = ""
    private var phoneNumber: String = ""

    // Function to initiate OTP verification
    fun initiatePhoneVerification(
        userFirstName: String,
        userLastName: String,
        userPhoneNumber: String,
        activity: Activity
    ) {
        // Store user data for later use
        firstName = userFirstName
        lastName = userLastName
        phoneNumber = userPhoneNumber

        _otpState.value = OtpState.Loading

        Log.d("AuthViewModel", "Initiating phone verification for: $phoneNumber (firstName: $firstName, lastName: $lastName)")

        // Validate the phone number before proceeding
        if (phoneNumber.isBlank()) {
            Log.e("AuthViewModel", "Phone number is blank")
            _otpState.value = OtpState.Error("Phone number cannot be empty")
            return
        }

        viewModelScope.launch {
            firebaseAuthService.sendOtp(phoneNumber, activity).collectLatest { result ->
                when (result) {
                    is OtpResult.CodeSent -> {
                        Log.d("AuthViewModel", "OTP sent successfully")
                        _otpState.value = OtpState.OtpSent
                    }
                    is OtpResult.VerificationCompleted -> {
                        Log.d("AuthViewModel", "Verification completed automatically")
                        // Auto-verification completed (rare on most devices)
                        _otpState.value = OtpState.AutoVerified
                        // Proceed with user registration on API
                        registerUserOnApi()
                    }
                    is OtpResult.VerificationFailed -> {
                        Log.e("AuthViewModel", "Verification failed: ${result.message}")
                        _otpState.value = OtpState.Error(result.message)
                    }
                }
            }
        }
    }

    // Function to verify OTP entered by the user
    fun verifyOtp(otp: String) {
        _otpState.value = OtpState.Loading

        Log.d("AuthViewModel", "Verifying OTP: $otp")

        viewModelScope.launch {
            firebaseAuthService.verifyOtp(otp).collectLatest { result ->
                when (result) {
                    is OtpVerificationResult.Success -> {
                        Log.d("AuthViewModel", "OTP verification successful")
                        _otpState.value = OtpState.Verified(result.userId)
                        // Once OTP is verified, register user with your API
                        registerUserOnApi()
                    }
                    is OtpVerificationResult.Error -> {
                        Log.e("AuthViewModel", "OTP verification error: ${result.message}")
                        _otpState.value = OtpState.Error(result.message)
                    }
                }
            }
        }
    }

    // Function to register user with your API after OTP verification
    private fun registerUserOnApi() {
        _registrationState.value = RegistrationState.Loading

        Log.d("AuthViewModel", "Registering user on API: $firstName $lastName")

        viewModelScope.launch {
            when (val response = apiService.registerUser(firstName, lastName)) {
                is ApiResponse.Success -> {
                    Log.d("AuthViewModel", "API registration successful: ${response.data.message}")
                    _registrationState.value = RegistrationState.Success(
                        response.data.message ?: "Registration successful"
                    )
                }
                is ApiResponse.Error -> {
                    Log.e("AuthViewModel", "API registration error: ${response.message}")
                    _registrationState.value = RegistrationState.Error(response.message)
                }
            }
        }
    }

    // Reset states
    fun resetOtpState() {
        _otpState.value = OtpState.Idle
    }

    fun resetRegistrationState() {
        _registrationState.value = RegistrationState.Idle
    }

    // Clean up when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        apiService.close()
    }

    // State classes for OTP verification
    sealed class OtpState {
        object Idle : OtpState()
        object Loading : OtpState()
        object OtpSent : OtpState()
        object AutoVerified : OtpState()
        data class Verified(val userId: String) : OtpState()
        data class Error(val message: String) : OtpState()
    }

    // State classes for registration process
    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        data class Success(val message: String) : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }
}