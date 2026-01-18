package com.example.skillsharex.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val status: String, // "success" or "error"
    val message: String
)