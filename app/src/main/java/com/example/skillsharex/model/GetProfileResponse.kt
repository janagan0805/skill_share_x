package com.example.skillsharex.model

data class GetProfileResponse(
    val success: Boolean,
    val data: ProfileData?
)

data class ProfileData(
    val name: String,
    val role: String,
    val profile_image: String,
    val skills: List<String>
)

data class UpdateProfileRequest(
    val user_id: Int,
    val full_name: String,
    val role: String,
    val skills: List<String>
)

