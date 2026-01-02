
package com.example.skillsharex.model

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val userRole: String, // "Mentor" or "Learner"
    val userAvatarUrl: String,
    val timestamp: Long,
    val content: String,
    val imageUrl: String? = null,
    val skillTag: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false
)
