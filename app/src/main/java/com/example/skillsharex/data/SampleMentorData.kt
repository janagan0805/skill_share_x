package com.example.skillsharex.data

import com.example.skillsharex.model.MentorDetail

val sampleMentorDetails = listOf(

    MentorDetail(
        id = 1,
        name = "Karthik",
        skill = "Android Development",
        rating = 4.9,
        ratingCount = 120,
        bio = "Android developer with 6+ years of experience building scalable apps using Kotlin and Jetpack Compose.",
        experienceYears = 6,
        expertiseList = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines"),
        image = "uploads/mentors/karthik.png",
        status = "online"
    ),

    MentorDetail(
        id = 2,
        name = "Dilip",
        skill = "UI/UX Design",
        rating = 4.8,
        ratingCount = 95,
        bio = "UI/UX designer specializing in mobile-first experiences with strong usability principles.",
        experienceYears = 5,
        expertiseList = listOf("Figma", "Prototyping", "Design Systems"),
        image = "uploads/mentors/dilip.png",
        status = "offline"
    ),

    MentorDetail(
        id = 3,
        name = "Pranav",
        skill = "Java Backend",
        rating = 4.7,
        ratingCount = 80,
        bio = "Backend engineer with deep expertise in Java, APIs, and system design.",
        experienceYears = 3,
        expertiseList = listOf("Java", "Spring Boot", "Docker", "Gradle"),
        image = "uploads/mentors/pranav.png",
        status = "online"
    ),

    MentorDetail(
        id = 4,
        name = "Gowtham",
        skill = "Graphic Design",
        rating = 4.6,
        ratingCount = 70,
        bio = "Graphic designer focused on branding, visual storytelling, and digital creatives.",
        experienceYears = 2,
        expertiseList = listOf("Photoshop", "Illustrator", "Brand Design"),
        image = "uploads/mentors/gowtham.png",
        status = "offline"
    ),

    MentorDetail(
        id = 5,
        name = "Dhanush",
        skill = "Photoshop",
        rating = 4.7,
        ratingCount = 60,
        bio = "Photoshop artist with hands-on experience in photo manipulation and social media creatives.",
        experienceYears = 2,
        expertiseList = listOf("Retouching", "Photo Manipulation", "Banner Design"),
        image = "uploads/mentors/dhanush.png",
        status = "online"
    )
)
