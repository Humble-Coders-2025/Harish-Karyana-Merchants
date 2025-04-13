package com.example.harishkaryanamerchants.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

/**
 * Helper class for Firebase operations
 */
object FirebaseHelper {
    /**
     * Checks if Firebase is initialized, attempts to initialize if not
     * @return true if Firebase is initialized or initialization was successful
     */
    fun ensureInitialized(context: Context): Boolean {
        return try {
            // Check if Firebase is already initialized
            FirebaseAuth.getInstance()
            true
        } catch (e: IllegalStateException) {
            // Firebase not initialized, try to initialize it
            try {
                FirebaseApp.initializeApp(context)
                true
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Failed to initialize Firebase: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Firebase error: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}