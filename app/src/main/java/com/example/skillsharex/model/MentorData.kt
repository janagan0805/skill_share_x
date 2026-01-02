package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

// Response wrapper for the top mentors API call
data class TopMentorsResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: List<MentorData>
)

// Represents a single mentor from the backend
data class MentorData(
    val id: String,
    val name: String,
    val status: String,
    val imageUrl: String?,
    val rating: Double,
    val rating_count: Int,
    val skill: String
)
