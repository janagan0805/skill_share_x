package com.example.skillsharex.data.model

import com.google.gson.annotations.SerializedName

data class Mentor(
    @SerializedName("name")
    val name: String,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("image")
    val image: String? = null
)
