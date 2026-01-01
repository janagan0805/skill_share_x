package com.example.skillsharex.ui.community

data class Post(
    val post_id: Int,
    val content: String,
    val like_count: Int,
    val is_liked: Boolean
)

data class CreatePostRequest(
    val user_id: Int,
    val content: String
)

data class LikeRequest(
    val post_id: Int,
    val user_id: Int
)
