package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

// Response wrapper for the top mentors API call
data class TopMentorsResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: List<MentorData>
)

// Represents a single mentor from the backend
data class MentorData(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val name: String,
    @SerializedName("primary_skill") val skill: String, // Assuming API provides a primary skill
    @SerializedName("profile_image_url") val imageUrl: String?,
    @SerializedName("status") val status: String, // "online" or "offline"
    @SerializedName("rating") val rating: Double
)
