package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

// Response wrapper for the course detail API call
data class CourseDetailResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: CourseDetail?
)

// Represents the detailed information for a single course
data class CourseDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String, // Changed from courseName
    @SerializedName("mentor_name") val mentorName: String,
    @SerializedName("description") val description: String,
    @SerializedName("image_path") val coverImage: String?, // Changed from cover_image
    @SerializedName("rating") val rating: Double,
    @SerializedName("total_hours") val totalHours: String,
    @SerializedName("lessons") val lessons: List<String>?
)
