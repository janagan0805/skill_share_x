package com.example.skillsharex.model

data class MentorshipRequest(
    val requestId: String,
    val userAvatar: Int,
    val userName: String,
    val timeAgo: String,
    val requestedSkill: String,
    val message: String,
    var status: String = "Pending",
    var rejectionReason: String? = null// Pending, Accepted, Rejected
)
