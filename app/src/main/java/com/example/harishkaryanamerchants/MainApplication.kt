// In MainApplication.kt - Single source of Firebase initialization
package com.example.harishkaryanamerchants

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d("MainApplication", "Firebase initialized successfully")
            } else {
                Log.d("MainApplication", "Firebase already initialized")
            }
        } catch (e: Exception) {
            Log.e("MainApplication", "Firebase initialization failed: ${e.message}", e)
        }
    }

    companion object {
        // This function should only be used if absolutely necessary,
        // such as when a component needs to ensure Firebase is initialized
        // before the application class has had a chance to do so.
        fun ensureFirebaseInitialized(application: Application): Boolean {
            try {
                if (FirebaseApp.getApps(application).isEmpty()) {
                    FirebaseApp.initializeApp(application)
                    Log.d("MainApplication", "Firebase initialized manually")
                    return true
                }
                // Firebase is already initialized
                return true
            } catch (e: Exception) {
                Log.e("MainApplication", "Firebase initialization failed: ${e.message}", e)
                return false
            }
        }
    }
}