package com.example.skillsharex.data

import com.example.skillsharex.R

data class CourseInfo(
    val id: String,
    val title: String,
    val banner: Int,
    val mentorName: String,
    val mentorRole: String,
    val rating: Double,
    val learners: String,
    val tags: List<String>,
    val about: String
)

val allCourses = listOf(
    CourseInfo(
        id = "1",
        title = "UI/UX Design",
        banner = R.drawable.ic_ui_ux_design,
        mentorName = "Saranraj",
        mentorRole = "UI/UX Specialist",
        rating = 4.9,
        learners = "1.2k+",
        tags = listOf("UX", "Wireframes", "Figma", "Design Systems"),
        about = "Master UI/UX fundamentals and build stunning user-centered interfaces."
    ),

    CourseInfo(
        id = "2",
        title = "Android Development",
        banner = R.drawable.android,
        mentorName = "Karthick",
        mentorRole = "Android Developer",
        rating = 4.8,
        learners = "950+",
        tags = listOf("Kotlin", "Jetpack Compose", "Navigation", "UI Design"),
        about = "Learn Android development using Kotlin and Jetpack Compose with real projects."
    ),

    CourseInfo(
        id = "3",
        title = "Web Development",
        banner = R.drawable.ic_web,
        mentorName = "Sriram",
        mentorRole = "Web Developer",
        rating = 4.7,
        learners = "2k+",
        tags = listOf("HTML", "CSS", "JavaScript", "React"),
        about = "Build fully responsive websites and master modern web development tools."
    ),

    CourseInfo(
        id = "4",
        title = "Java Programming",
        banner = R.drawable.ic_java,
        mentorName = "Pranav",
        mentorRole = "Java Trainer",
        rating = 4.8,
        learners = "1.4k+",
        tags = listOf("OOP", "DSA", "Spring Boot", "Backend"),
        about = "Learn Java from basics to advanced with real-time coding practices."
    ),

    CourseInfo(
        id = "5",
        title = "Graphic Designing",
        banner = R.drawable.ic_graphics,
        mentorName = "Gowtham",
        mentorRole = "Graphic Designer",
        rating = 4.6,
        learners = "780+",
        tags = listOf("Poster Design", "Color Theory", "Branding"),
        about = "Learn graphic design fundamentals and create eye-catching visuals."
    ),

    CourseInfo(
        id = "6",
        title = "Python Basics",
        banner = R.drawable.ic_python,
        mentorName = "Dilip",
        mentorRole = "Python Developer",
        rating = 4.9,
        learners = "3k+",
        tags = listOf("Python", "AI Basics", "Automation"),
        about = "Python for beginners—learn syntax, logic, automation and more."
    ),

    CourseInfo(
        id = "7",
        title = "Photoshop Editing",
        banner = R.drawable.ic_photoshop,
        mentorName = "Dhanush",
        mentorRole = "Photoshop Artist",
        rating = 4.5,
        learners = "600+",
        tags = listOf("Retouching", "Photo Editing", "Poster Design"),
        about = "Learn Photoshop tools, layers, blending modes & professional editing."
    ),

    CourseInfo(
        id = "8",
        title = "Figma Mastery",
        banner = R.drawable.ic_figma,
        mentorName = "Dhanacheziyan",
        mentorRole = "Product Designer",
        rating = 4.9,
        learners = "1k+",
        tags = listOf("Figma", "Components", "Prototyping"),
        about = "Master Figma workflows, design systems, components and prototyping."
    ),

    CourseInfo(
        id = "9",
        title = "Communication Skills",
        banner = R.drawable.ic_communication,
        mentorName = "Suriya",
        mentorRole = "Soft Skills Trainer",
        rating = 4.8,
        learners = "1.7k+",
        tags = listOf("Public Speaking", "Confidence", "Leadership"),
        about = "Improve communication skills, confidence, presentation & leadership."
    ),
)
