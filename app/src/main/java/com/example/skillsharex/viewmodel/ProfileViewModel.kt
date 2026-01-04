package com.example.skillsharex.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileViewModel : ViewModel() {

    val userId: Int = 1 // replace later with SessionManager

    val name = mutableStateOf("Jana")
    val role = mutableStateOf("Mentor • SkillShareX")
    val bio = mutableStateOf("Helping learners grow in Design & Tech 🚀")

    val skills = mutableStateListOf(
        "UI/UX",
        "Java",
        "Figma",
        "Photoshop"
    )

    val profileImageUrl = mutableStateOf<String?>(null)

    fun uploadProfileImage(
        imagePath: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val file = File(imagePath)

                val requestFile =
                    file.readBytes().toRequestBody("image/*".toMediaType())

                val imagePart =
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestFile
                    )

                val userIdBody =
                    userId.toString().toRequestBody("text/plain".toMediaType())

                val response =
                    AuthApiClient.api.uploadProfileImage(imagePart, userIdBody)

                if (response.isSuccessful && response.body()?.status == true) {
                    profileImageUrl.value = response.body()?.image_url
                    onResult(true, response.body()?.image_url)
                } else {
                    onResult(false, response.body()?.message)
                }

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
