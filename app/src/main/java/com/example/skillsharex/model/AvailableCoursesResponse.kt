package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

data class AvailableCoursesResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<CourseData>
)
