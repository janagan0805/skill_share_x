package com.example.skillsharex.model

data class OnlineMentorsResponse(
    val status: Boolean,
    val message: String,
    val data: List<OnlineMentor>
)
