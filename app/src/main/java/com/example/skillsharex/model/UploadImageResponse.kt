package com.example.skillsharex.data.models

data class UploadImageResponse(
    val status: Boolean,
    val message: String,
    val data: ImageData?
)

data class ImageData(
    val image_url: String
)
