package com.example.skillsharex.data.models

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User? = null
)

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val profil_image: String
)
