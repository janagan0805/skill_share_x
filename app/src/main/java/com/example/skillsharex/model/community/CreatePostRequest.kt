package com.example.skillsharex.model.community

data class CreatePostRequest(
    val user_id: Int,
    val post_type: String,
    val skill_id: String?, // backend handles string → ID
    val title: String?,
    val description: String
)
