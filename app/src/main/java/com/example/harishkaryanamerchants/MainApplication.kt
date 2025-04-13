package com.example.harishkaryanamerchants

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            FirebaseApp.initializeApp(applicationContext)
            Log.d("MainApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MainApplication", "Firebase initialization failed: ${e.message}", e)
        }
    }

    companion object {
        fun initializeFirebaseIfNeeded(application: Application) {
            try {
                if (FirebaseApp.getApps(application).isEmpty()) {
                    FirebaseApp.initializeApp(application)
                    Log.d("MainApplication", "Firebase initialized successfully from helper")
                } else {
                    Log.d("MainApplication", "Firebase already initialized")
                }
            } catch (e: Exception) {
                Log.e("MainApplication", "Firebase initialization failed: ${e.message}", e)
            }
        }
    }
}