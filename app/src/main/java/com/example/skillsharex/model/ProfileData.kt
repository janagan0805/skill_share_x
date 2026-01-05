package com.example.skillsharex.data.model

data class ProfileData(
    val name: String,
    val role: String,
    val bio: String,
    val profile_image: String?,
    val skills: List<String>
)
