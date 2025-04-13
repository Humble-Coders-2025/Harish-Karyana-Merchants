package com.example.harishkaryanamerchants.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.harishkaryanamerchants.screens.greetingScreen.GreetingScreen
import com.example.harishkaryanamerchants.screens.homeScreen.HomeScreen
import com.example.harishkaryanamerchants.screens.loginOrResisterScreen.LoginScreen
import com.example.harishkaryanamerchants.screens.otpScreen.OTPScreenUI
import com.example.harishkaryanamerchants.screens.registerScreen.RegisterScreenUI

// Define route constants
object AppDestinations {
    const val GREETING_SCREEN = "greeting_screen"
    const val LOGIN_REGISTER_SCREEN = "login_register_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val OTP_SCREEN = "otp_screen"
    const val HOME_SCREEN = "home_screen"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.GREETING_SCREEN
    ) {
        composable(AppDestinations.GREETING_SCREEN) {
            GreetingScreen(
                onGetStartedClick = {
                    navController.navigate(AppDestinations.LOGIN_REGISTER_SCREEN)
                }
            )
        }

        composable(AppDestinations.LOGIN_REGISTER_SCREEN) {
            LoginScreen(
                onLoginClick = {
                    // Login flow - for now, directly go to home (would be different with authentication)
                    navController.navigate(AppDestinations.HOME_SCREEN) {
                        popUpTo(AppDestinations.GREETING_SCREEN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppDestinations.REGISTER_SCREEN)
                }
            )
        }

        composable(AppDestinations.REGISTER_SCREEN) {
            RegisterScreenUI(
                onBackClick = {
                    navController.popBackStack()
                },
                onSendOtpClick = { firstName, lastName, phoneNumber ->
                    // Using Bundle for safety instead of URL parameters
                    navController.currentBackStackEntry?.arguments?.putString("firstName", firstName)
                    navController.currentBackStackEntry?.arguments?.putString("lastName", lastName)
                    navController.currentBackStackEntry?.arguments?.putString("phoneNumber", phoneNumber)

                    // Navigate to OTP screen
                    navController.navigate(AppDestinations.OTP_SCREEN)
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppDestinations.OTP_SCREEN) {
            // Get the user data from the previous screen's arguments
            val firstName = navController.previousBackStackEntry?.arguments?.getString("firstName") ?: ""
            val lastName = navController.previousBackStackEntry?.arguments?.getString("lastName") ?: ""
            val phoneNumber = navController.previousBackStackEntry?.arguments?.getString("phoneNumber") ?: ""

            val userData = UserData(firstName, lastName, phoneNumber)

            OTPScreenUI(
                userData = userData,
                onBackClick = {
                    navController.popBackStack()
                },
                onRegistrationComplete = {
                    // Navigate to home screen and clear all previous screens from back stack
                    navController.navigate(AppDestinations.HOME_SCREEN) {
                        popUpTo(AppDestinations.GREETING_SCREEN) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(AppDestinations.LOGIN_REGISTER_SCREEN) {
                        popUpTo(AppDestinations.LOGIN_REGISTER_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.HOME_SCREEN) {
            HomeScreen()
        }
    }
}

// Data class for passing user information between screens
data class UserData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)