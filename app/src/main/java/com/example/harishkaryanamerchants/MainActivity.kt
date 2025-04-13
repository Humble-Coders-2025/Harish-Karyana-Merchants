package com.example.harishkaryanamerchants

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.harishkaryanamerchants.navigation.AppNavigation
import com.example.harishkaryanamerchants.ui.theme.HarishKaryanaMerchantsTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure Firebase is initialized
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d("FirebaseInit", "Firebase initialized in MainActivity")
            }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Firebase init failed in MainActivity: ${e.message}")
        }

        setContent {
            HarishKaryanaMerchantsTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}