package com.example.skillsharex.data

import com.example.skillsharex.R
import com.example.skillsharex.model.CourseBrief
import com.example.skillsharex.model.MentorDetail
import com.example.skillsharex.model.Review

val sampleMentorDetails = listOf(

    MentorDetail(
        mentorId = "1",
        name = "Karthik",
        skill = "Android Developer",
        rating = 4.9,
        bio = "Android developer with 6+ years building production apps using Kotlin and Jetpack Compose.",
        experienceYears = 6,
        expertiseList = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines"),
        profileImageRes = R.drawable.karthick,
        courses = listOf(
            CourseBrief("c1", "Android Development Basics", 4.8, R.drawable.android),
            CourseBrief("c2", "Advanced Compose UI", 4.7, R.drawable.ic_ui_ux_design)
        ),
        reviews = listOf(
            Review("Anita", 5.0, "Explains concepts clearly."),
            Review("Vikram", 4.8, "Great hands-on practice.")
        )
    ),

    MentorDetail(
        mentorId = "2",
        name = "Dilip",
        skill = "UI/UX Designer",
        rating = 4.8,
        bio = "Designer focused on mobile UI and usability with 5+ years experience.",
        experienceYears = 5,
        expertiseList = listOf("Figma", "Prototyping", "Visual Design"),
        profileImageRes = R.drawable.dilip,
        courses = listOf(
            CourseBrief("c3", "Creative UI/UX Design", 4.7, R.drawable.ic_ui_ux_design)
        ),
        reviews = listOf(
            Review("Saran", 4.9, "Highly practical sessions."),
            Review("Meena", 4.6, "Very helpful.")
        )
    ),

    MentorDetail(
        mentorId = "3",
        name = "Pranav",
        skill = "Java Expert",
        rating = 4.8,
        bio = "Java backend expert with strong real-world teaching approach.",
        experienceYears = 3,
        expertiseList = listOf("Java", "Backend", "Docker", "Gradle"),
        profileImageRes = R.drawable.pranav,
        courses = listOf(
            CourseBrief("c4", "Java Masterclass", 4.7, R.drawable.ic_java)
        ),
        reviews = listOf(
            Review("Vijay", 4.9, "Amazing learning experience."),
            Review("Ashwin", 4.6, "Clear explanations.")
        )
    ),

    MentorDetail(
        mentorId = "4",
        name = "Gowtham",
        skill = "Graphic Designer",
        rating = 4.8,
        bio = "Graphic designer with 2.5+ years experience crafting brand visuals.",
        experienceYears = 2,
        expertiseList = listOf("VFX", "Animation", "Visual Design", "Gaming"),
        profileImageRes = R.drawable.gowtham,
        courses = listOf(
            CourseBrief("c5", "Graphics Design", 4.7, R.drawable.ic_graphics)
        ),
        reviews = listOf(
            Review("Sandhiya", 4.9, "Great for logo making."),
            Review("Dhanacheziyan", 4.8, "Helps develop gaming graphics.")
        )
    ),

    MentorDetail(
        mentorId = "5",
        name = "Dhanush",
        skill = "Photoshop Artist",
        rating = 4.7,
        bio = "Photoshop expert with 2+ years designing banners, posters, and social media creatives.",
        experienceYears = 2,
        expertiseList = listOf("Retouching", "Manipulation", "Product Editing"),
        profileImageRes = R.drawable.dhanush,
        courses = listOf(
            CourseBrief("c6", "Photoshop Essentials", 4.7, R.drawable.ic_photoshop)
        ),
        reviews = listOf(
            Review("Lakshmi", 4.9, "Very creative lessons."),
            Review("Rithika", 4.8, "Helpful for editing.")
        )
    )
)
