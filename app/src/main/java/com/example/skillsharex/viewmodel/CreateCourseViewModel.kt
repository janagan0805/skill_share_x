package com.example.skillsharex.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class CreateCourseViewModel : ViewModel() {

    val title = mutableStateOf("")
    val description = mutableStateOf("")

    // ✅ MUST MATCH BACKEND
    val status = mutableStateOf("inactive") // active | inactive

    val imageUri = mutableStateOf<Uri?>(null)

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val isSuccess = mutableStateOf(false)

    private var sessionManager: SessionManager? = null

    fun initSession(context: Context) {
        sessionManager = SessionManager(context)
    }

    fun setImage(uri: Uri) {
        imageUri.value = uri
    }

    fun createCourse(context: Context) {
        val userId = sessionManager?.getUserId()
        if (userId == null) {
            errorMessage.value = "User not logged in"
            return
        }

        if (title.value.isBlank()) {
            errorMessage.value = "Course title is required"
            return
        }

        if (imageUri.value == null) {
            errorMessage.value = "Course image is required"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val uri = imageUri.value!!

                val mimeType =
                    context.contentResolver.getType(uri) ?: "image/jpeg"

                val inputStream =
                    context.contentResolver.openInputStream(uri)
                        ?: throw IllegalStateException("Cannot open image")

                val bytes = inputStream.readBytes()

                val requestBody =
                    bytes.toRequestBody(mimeType.toMediaType())

                val extension = when (mimeType) {
                    "image/png" -> "png"
                    "image/webp" -> "webp"
                    else -> "jpg"
                }

                val imagePart =
                    MultipartBody.Part.createFormData(
                        "image",
                        "course_${System.currentTimeMillis()}.$extension",
                        requestBody
                    )

                val response = AuthApiClient.api.createCourse(
                    userId = userId.toString().toRequestBody("text/plain".toMediaType()),
                    title = title.value.trim().toRequestBody("text/plain".toMediaType()),
                    description = description.value.trim().toRequestBody("text/plain".toMediaType()),
                    status = status.value.toRequestBody("text/plain".toMediaType()),
                    image = imagePart
                )

                if (response.status) {
                    isSuccess.value = true
                } else {
                    // ✅ SHOW BACKEND MESSAGE DIRECTLY
                    errorMessage.value =
                        response.message ?: "Unable to create course"
                }

            } catch (e: Exception) {
                errorMessage.value = "Upload failed: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
