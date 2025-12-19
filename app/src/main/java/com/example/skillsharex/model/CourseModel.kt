package com.example.skillsharex.model

data class Course(
    val courseId: String,
    val title: String,
    val description: String,
    val mentorName: String,
    val rating: Double,
    val category: String,
    val lessonsCount: Int,
    val level: String,
    val thumbnail: Int
)

data class Mentor(
    val mentorId: String,
    val name: String,
    val skill: String,
    val rating: Double,
    val image: Int
)