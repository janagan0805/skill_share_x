package com.example.skillsharex.model

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User? = null
)

data class User(
    val id: Int,
    val full_name: String,
    val email: String
)
