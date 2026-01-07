package com.example.skillsharex.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.data.model.ProfileData
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel : ViewModel() {

    /* ---------------- SESSION ---------------- */

    private var sessionManager: SessionManager? = null

    fun initSession(context: Context) {
        if (sessionManager == null) {
            sessionManager = SessionManager(context)

            // ✅ LOAD CACHED IMAGE IMMEDIATELY (IMPORTANT)
            sessionManager?.getProfileImageUrl()?.let { path ->
                profileImageUrl.value =
                    AuthApiClient.IMAGE_BASE_URL + path
            }
        }
    }

    /* ---------------- PROFILE STATE ---------------- */

    val name = mutableStateOf("")
    val role = mutableStateOf("")
    val bio = mutableStateOf("")

    val skills = mutableStateListOf<String>()
    val isLoading = mutableStateOf(false)
    val isUploading = mutableStateOf(false)   // ✅ FIXED
    val errorMessage = mutableStateOf<String?>(null)

    // Profile data
    val profileData = mutableStateOf<ProfileData?>(null)


    // ✅ SINGLE SOURCE OF TRUTH
    val profileImageUrl = mutableStateOf<String?>(null)

    /* ----------------------------------------------------
       FETCH PROFILE (SERVER → VIEWMODEL → UI)
    ---------------------------------------------------- */
    fun fetchProfile() {
        val userId = sessionManager?.getUserId() ?: return

        viewModelScope.launch {
            try {
                val response = AuthApiClient.api.getProfile(userId)

                if (!response.isSuccessful) return@launch
                val body = response.body() ?: return@launch
                if (!body.success) return@launch

                val data = body.data

                name.value = data?.name ?: ""
                role.value = data?.role ?: ""
                bio.value = data?.bio ?: ""

                // ✅ USE SERVER IMAGE IF AVAILABLE
                val imagePath =
                    data?.profile_image
                        ?: sessionManager?.getProfileImageUrl()

                profileImageUrl.value =
                    imagePath?.let {
                        AuthApiClient.IMAGE_BASE_URL + it
                    }

                // ✅ SAVE FOR NEXT APP OPEN
                imagePath?.let {
                    sessionManager?.saveProfileImageUrl(it)
                }


                skills.clear()
                data?.skills?.let { skills.addAll(it) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* ----------------------------------------------------
       UPLOAD PROFILE IMAGE
    ---------------------------------------------------- */
    fun uploadProfileImage(
        context: Context,
        imageUri: Uri
    ) {
        val userId = sessionManager?.getUserId() ?: return

        viewModelScope.launch {
            try {
                val inputStream =
                    context.contentResolver.openInputStream(imageUri) ?: return@launch

                val bytes = inputStream.readBytes()

                val requestBody =
                    bytes.toRequestBody("image/*".toMediaType())

                val imagePart =
                    MultipartBody.Part.createFormData(
                        "image",
                        "profile_$userId.jpg",
                        requestBody
                    )

                val userIdBody =
                    userId.toString()
                        .toRequestBody("text/plain".toMediaType())

                val response =
                    AuthApiClient.api.uploadProfileImage(
                        image = imagePart,
                        userId = userIdBody
                    )

                if (response.isSuccessful) {
                    val body = response.body() ?: return@launch
                    if (!body.success) return@launch

                    // ✅ 👉 PASTE HERE (THIS IS THE PLACE)
                    val imagePath = body.data?.image_url
                    sessionManager?.saveProfileImageUrl(imagePath ?: "")

                    profileImageUrl.value =
                        imagePath?.let{ AuthApiClient.IMAGE_BASE_URL + it }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
