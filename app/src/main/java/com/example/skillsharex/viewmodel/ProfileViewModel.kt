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

    val userId: Int = 1 // TODO replace later with SessionManager

    val name = mutableStateOf("Jana")
    val role = mutableStateOf("Mentor • SkillShareX")
    val bio = mutableStateOf("Helping learners grow in Design & Tech 🚀")

    val skills = mutableStateListOf(
        "UI/UX",
        "Java",
        "Figma",
        "Photoshop"
    )

    // ✅ SINGLE SOURCE OF TRUTH
    val profileImageUrl = mutableStateOf<String?>(null)

    /* ----------------------------------------------------
       FETCH PROFILE (FROM DATABASE → UI)
       Call this after login or in ProfileScreen LaunchedEffect
    ---------------------------------------------------- */
    fun fetchProfile(userId: Int) {
        viewModelScope.launch {
            try {
                val response = AuthApiClient.api.getProfile(userId)

                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()!!.data

                    name.value = data.name
                    role.value = data.role
                    bio.value = data.bio

                    profileImageUrl.value =
                        "http://172.25.105.154/skillsharex_backend/${data.profile_image}"

                    skills.clear()
                    skills.addAll(data.skills)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* ----------------------------------------------------
       UPLOAD PROFILE IMAGE
       Called from EditProfileScreen
    ---------------------------------------------------- */
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
                    response.body()?.image_url?.let { url ->
                        profileImageUrl.value =
                            "http://172.25.105.154/skillsharex_backend/$url"
                        onResult(true, url)
                    }
                } else {
                    onResult(false, response.body()?.message)
                }

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
