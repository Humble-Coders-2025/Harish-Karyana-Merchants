package com.example.harishkaryanamerchants.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class FirebaseAuthService {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // This variable will store the verification ID received when sending OTP
    private var storedVerificationId: String? = null

    // Function to send OTP to phone number
// Also update the sendOtp function to add more logging
    fun sendOtp(
        phoneNumber: String,
        activity: Activity
    ): Flow<OtpResult> = callbackFlow {
        try {
            // Ensure Firebase is initialized
            if (FirebaseApp.getApps(activity).isEmpty()) {
                FirebaseApp.initializeApp(activity)
                Log.d("FirebaseAuth", "Firebase initialized in sendOtp")
            }

            Log.d("FirebaseAuth", "Attempting to send OTP to number: $phoneNumber")

            // Format the phone number to E.164 format
            val formattedNumber = try {
                formatPhoneNumber(phoneNumber)
            } catch (e: IllegalArgumentException) {
                Log.e("FirebaseAuth", "Phone formatting error: ${e.message}")
                trySend(OtpResult.VerificationFailed(e.message ?: "Invalid phone number format"))
                close()
                return@callbackFlow
            }

            Log.d("FirebaseAuth", "Sending OTP to: $formattedNumber")

            // Rest of the function remains the same
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This will be invoked if the phone number is instantly verified
                    // without needing an OTP (e.g., the phone is on the same device)
                    Log.d("FirebaseAuth", "Verification completed automatically")
                    trySend(OtpResult.VerificationCompleted(credential))
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    // Handle verification failures - THIS IS WHERE THE ERROR IS HAPPENING
                    Log.e("FirebaseAuth", "Verification failed: ${exception.message}", exception)

                    // Instead of throwing NotImplementedError, properly handle the error
                    val errorMessage = when {
                        exception.message?.contains("BILLING_NOT_ENABLED") == true ->
                            "Firebase Phone Auth requires billing to be enabled in your Firebase project. Please check your Firebase console."
                        else -> exception.message ?: "Verification failed"
                    }

                    trySend(OtpResult.VerificationFailed(errorMessage))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Code has been sent to the phone number
                    Log.d("FirebaseAuth", "OTP code sent successfully")
                    storedVerificationId = verificationId
                    trySend(OtpResult.CodeSent(verificationId, token))
                }
            }

            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(formattedNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error sending OTP: ${e.message}", e)
            trySend(OtpResult.VerificationFailed("Error sending OTP: ${e.message}"))
            close()
        }

        awaitClose { /* Clean up if needed */ }
    }

    // Function to verify the OTP entered by the user
    fun verifyOtp(otp: String): Flow<OtpVerificationResult> = callbackFlow {
        try {
            if (storedVerificationId.isNullOrEmpty()) {
                Log.e("FirebaseAuth", "Verification ID not found")
                trySend(OtpVerificationResult.Error("Verification ID not found. Please request OTP again."))
                close()
                return@callbackFlow
            }

            Log.d("FirebaseAuth", "Attempting to verify OTP: $otp with ID: $storedVerificationId")
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User successfully verified with OTP
                        val user = auth.currentUser
                        Log.d("FirebaseAuth", "OTP verification successful. User ID: ${user?.uid}")
                        trySend(OtpVerificationResult.Success(user?.uid ?: ""))
                    } else {
                        // Verification failed
                        Log.e("FirebaseAuth", "OTP verification failed: ${task.exception?.message}")
                        trySend(OtpVerificationResult.Error(task.exception?.message ?: "Verification failed"))
                    }
                    close()
                }

        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error verifying OTP: ${e.message}", e)
            trySend(OtpVerificationResult.Error("Error verifying OTP: ${e.message}"))
            close()
        }

        awaitClose { /* Clean up if needed */ }
    }

    // Helper function to ensure E.164 format for phone numbers
    // Updated formatPhoneNumber function for FirebaseAuthService.kt
    // Update the formatPhoneNumber function in FirebaseAuthService.kt
    private fun formatPhoneNumber(phoneNumber: String): String {
        // Check for empty phone number first
        if (phoneNumber.isBlank()) {
            Log.e("FirebaseAuth", "Received blank phone number for formatting")
            throw IllegalArgumentException("Phone number cannot be empty")
        }

        // Remove all non-digit characters except the plus sign
        val cleanedNumber = phoneNumber.replace(Regex("[^0-9+]"), "")

        Log.d("FirebaseAuth", "Formatting phone number. Raw: $phoneNumber, Cleaned: $cleanedNumber")

        // If it already starts with +, return it (assuming it's already in E.164 format)
        if (cleanedNumber.startsWith("+")) {
            return cleanedNumber
        }

        // Remove any leading zeros
        val digitsOnly = cleanedNumber.replace(Regex("^0+"), "")
        Log.d("FirebaseAuth", "Digits only (no leading zeros): $digitsOnly")

        // Ensure we're dealing with an Indian number (10 digits)
        if (digitsOnly.length == 10) {
            return "+91$digitsOnly"  // Add Indian country code with plus
        } else if (digitsOnly.startsWith("91") && digitsOnly.length == 12) {
            return "+$digitsOnly"  // Just add plus if it already has country code
        } else {
            Log.e("FirebaseAuth", "Invalid phone number format: $digitsOnly (length: ${digitsOnly.length})")
            throw IllegalArgumentException("Please enter a valid 10-digit phone number")
        }
    }

    // Result classes for OTP operations
    sealed class OtpResult {
        data class VerificationCompleted(val credential: PhoneAuthCredential) : OtpResult()
        data class VerificationFailed(val message: String) : OtpResult()
        data class CodeSent(
            val verificationId: String,
            val token: PhoneAuthProvider.ForceResendingToken
        ) : OtpResult()
    }

    sealed class OtpVerificationResult {
        data class Success(val userId: String) : OtpVerificationResult()
        data class Error(val message: String) : OtpVerificationResult()
    }
}