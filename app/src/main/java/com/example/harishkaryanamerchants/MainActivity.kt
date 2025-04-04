package com.example.harishkaryanamerchants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.harishkaryanamerchants.screens.otpScreen.OTPScreenUI
import com.example.harishkaryanamerchants.screens.productScreen.ProductDetailScreen
import com.example.harishkaryanamerchants.ui.theme.HarishKaryanaMerchantsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarishKaryanaMerchantsTheme {
                ProductDetailScreen()
            }
        }
    }
}