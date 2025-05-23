plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose);
    id ("kotlin-parcelize")
    id ("com.google.gms.google-services") version "4.4.2";
}

android {
    namespace = "com.example.harishkaryanamerchants"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.harishkaryanamerchants"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.compose.material3:material3:1.3.1")

    implementation ("androidx.compose.material:material-icons-extended:<latest_version>")   //Extended Icons

        implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2") // For ViewModel in Compose
        implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // For lifecycle-aware components

    implementation(libs.accompanist.systemuicontroller);

    // splash screen
    implementation(libs.androidx.core.splashscreen)

    // Dependencies for synchronized scrolling(third party)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators);

    // Navigation for Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Latest as of April 2025

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")

// Ktor dependencies for API calls
    implementation ("io.ktor:ktor-client-core:2.3.7")
    implementation ("io.ktor:ktor-client-android:2.3.7")
    implementation ("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation ("io.ktor:ktor-client-logging:2.3.7")

// Kotlinx serialization
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

}