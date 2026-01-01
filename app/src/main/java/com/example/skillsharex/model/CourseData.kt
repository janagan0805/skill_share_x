package com.example.skillsharex.model

import com.google.gson.annotations.SerializedName

data class CourseData(
    @SerializedName("id") val id: Int,
    @SerializedName("course_name") val course_name: String,
    @SerializedName("mentor_name") val mentor_name: String,
    @SerializedName("cover_image") val cover_image: String?,
    @SerializedName("mentor_online_status") val mentor_online_status: String
)
