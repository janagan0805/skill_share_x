package com.example.skillsharex.data

import com.example.skillsharex.R
import com.example.skillsharex.model.MentorshipRequest

val sampleRequests = mutableListOf(
    MentorshipRequest(
        requestId = "r1",
        userAvatar = R.drawable.jana,
        userName = "Jana",
        timeAgo = "2h ago",
        requestedSkill = "Android Development",
        message = "Hi mentor, I would like to learn Android from basics. Please accept my request."
    ),
    MentorshipRequest(
        requestId = "r2",
        userAvatar = R.drawable.pranav,
        userName = "Pranav",
        timeAgo = "5h ago",
        requestedSkill = "UI/UX Design",
        message = "I want guidance for designing my portfolio. Kindly help me."
    ),
    MentorshipRequest(
        requestId = "r3",
        userAvatar = R.drawable.karthick,
        userName = "Karthick",
        timeAgo = "1d ago",
        requestedSkill = "Python",
        message = "I’m preparing for interviews. Need mentorship for Python & coding rounds."
    )
)
