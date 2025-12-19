package com.example.skillsharex.model

data class Review(
    val reviewerName: String,
    val rating: Double,
    val comment: String
)

data class CourseBrief(
    val courseId: String,
    val title: String,
    val rating: Double,
    val thumbnailRes: Int
)

data class MentorDetail(
    val mentorId: String,
    val name: String,
    val skill: String,
    val rating: Double,
    val bio: String,
    val experienceYears: Int,
    val expertiseList: List<String>,
    val profileImageRes: Int,
    val courses: List<CourseBrief>,
    val reviews: List<Review>
)

