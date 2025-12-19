package com.example.skillsharex.data

import com.example.skillsharex.R
import com.example.skillsharex.model.Course
import com.example.skillsharex.model.Mentor

val sampleCourses = listOf(
    Course(
        "1",
        "Android Development Basics",
        "Learn Kotlin and Jetpack Compose from scratch.",
        "Rahul Verma",
        4.8,
        "Programming",
        12,
        "Beginner",
        R.drawable.android
    ),
    Course(
        "2",
        "Creative UI/UX Design",
        "Master layouts, typography, and mobile UI design.",
        "Aisha Khan",
        4.7,
        "Design",
        10,
        "Intermediate",
        R.drawable.ic_ui_ux_design
    )
)

val sampleMentors = listOf(
    Mentor("1", "Karthik", "Android Developer", 4.9, R.drawable.karthick),
    Mentor("2", "Dilip", "UI/UX Designer", 4.8, R.drawable.dilip),
    Mentor("3", "Pranav", "Java Expert", 4.7, R.drawable.pranav),
    Mentor("4", "Gowtham", "Graphic Designer", 4.6, R.drawable.gowtham)
)