package com.example.harishkaryanamerchants.network


import android.os.Parcelable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

import kotlinx.parcelize.Parcelize

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        defaultRequest {
            url("https://supermarket.humblecoders.in/api/v1/")
            contentType(ContentType.Application.Json)
        }
    }

    // Register a new user
    suspend fun registerUser(firstName: String, lastName: String): ApiResponse<RegisterResponse> {
        return try {
            val response: RegisterResponse = client.post("customer/auth/register") {
                setBody(RegisterRequest(firstName, lastName))
            }.body()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error occurred")
        }
    }

    // Close the client when no longer needed
    fun close() {
        client.close()
    }
}

// Request and Response models
@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String
)

@Serializable
data class RegisterResponse(
    val success: Boolean = false,
    val message: String? = null,
    val data: UserData? = null
)

@Parcelize
data class UserData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
) : Parcelable

// Sealed class for API response handling
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
}