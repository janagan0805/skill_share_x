package com.example.skillsharex.ui.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    var name = mutableStateOf("Jana")
    var role = mutableStateOf("Mentor • SkillShareX")
    var bio = mutableStateOf("Helping learners grow in Design & Tech 🚀")

    val skills = mutableStateListOf(
        "UI/UX",
        "Java",
        "Figma",
        "Photoshop"
    )
}
