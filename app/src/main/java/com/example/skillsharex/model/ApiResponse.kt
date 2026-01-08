package com.example.skillsharex.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

data class BasicApiResponse(
    val status: String,
    val message: String
)
