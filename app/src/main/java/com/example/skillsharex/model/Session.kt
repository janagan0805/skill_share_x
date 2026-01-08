package com.example.skillsharex.data.model

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("skill")
    val skill: String? = null,

    @SerializedName("date")
    val date: String,

    @SerializedName("start_time")
    val start_time: String,

    @SerializedName("end_time")
    val end_time: String,

    @SerializedName("status")
    val status: String? = null,

    // 🔥 THIS WAS THE PROBLEM
    @SerializedName("mentor")
    val mentor: Mentor? = null
)


data class SessionListResponse(
    val status: Boolean,
    val message: String,
    val data: List<Session>
)

data class SessionDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Session
)