package com.example.skillsharex.data.model

data class GenericResponse(
    val status: Boolean,
    val message: String,
    val image_url: String? = null
)
