package com.example.skillsharex.model

data class UserResponse(
    val success: Boolean,
    val user: User?
)

data class User(
    val id: Int,
    val name: String,
    val role: String,
    val avatar_url: String?
)
data class CreatePostRequest(
    val user_id: Int,
    val title: String,
    val content: String,
    val topic: String,
)

data class CreatePostResponse(
    val success: Boolean,
    val post_id: Int?
)
