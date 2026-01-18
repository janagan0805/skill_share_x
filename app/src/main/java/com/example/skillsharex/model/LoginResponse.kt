package com.example.skillsharex.model

data class LoginResponse(
    val status: String,
    val message: String,
    val user: UserData? = null
)

data class UserData(
    val id: Int,
    val full_name: String,
    val email: String,
    val profil_image: String
)
