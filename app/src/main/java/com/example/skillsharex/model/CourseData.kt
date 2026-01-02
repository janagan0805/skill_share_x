package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

data class CourseData(
    val id: String,
    val course_name: String,
    val cover_image: String?,
    val rating: Double,
    val rating_count: Int,
    val mentor_name: String,
    val mentor_online_status: String
)
