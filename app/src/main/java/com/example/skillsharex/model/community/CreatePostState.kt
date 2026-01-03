package com.example.skillsharex.model.community

data class CreatePostState(
    val postType: String = "discussion",
    val title: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
