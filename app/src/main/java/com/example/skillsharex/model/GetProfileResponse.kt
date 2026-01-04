package com.example.skillsharex.model

data class GetProfileResponse(
    val status: Boolean,
    val data: ProfileData
)

data class ProfileData(
    val name: String,
    val role: String,
    val bio: String,
    val profile_image: String,
    val skills: List<String>
)
