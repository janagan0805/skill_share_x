package com.example.skillsharex.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProfileViewModel : ViewModel() {

    /* ---------------- SESSION ---------------- */

    private var sessionManager: SessionManager? = null

    /* ---------------- PROFILE STATE ---------------- */

    val name = mutableStateOf("")
    val role = mutableStateOf("")
    val bio = mutableStateOf("")

    val skills = mutableStateListOf<String>()

    // ✅ SINGLE SOURCE OF TRUTH
    val profileImageUrl = mutableStateOf<String?>(null)
    val isUploading = mutableStateOf(false)
    val uploadError = mutableStateOf<String?>(null)

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
    private fun resizeBitmap(
        bitmap: Bitmap,
        maxSize: Int = 1024
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scale = if (width > height) {
            maxSize.toFloat() / width
        } else {
            maxSize.toFloat() / height
        }

        return Bitmap.createScaledBitmap(
            bitmap,
            (width * scale).toInt(),
            (height * scale).toInt(),
            true
        )
    }



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

                name.value = data?.name.toString()
                role.value = data?.role.toString()
                bio.value = data?.bio.toString()

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
                    sessionManager?.saveProfileImageUri(it)
                }


                skills.clear()
                skills.addAll((data?.skills ?: " ") as Collection<String>)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* ----------------------------------------------------
       UPLOAD PROFILE IMAGE
    ---------------------------------------------------- */
    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadProfileImage(
        context: Context,
        imageUri: Uri
    ) {
        val userId = sessionManager?.getUserId()
            ?: throw IllegalStateException("Session not initialized")

        isUploading.value = true
        uploadError.value = null

        Log.e("PROFILE_UPLOAD", "USER ID = $userId")

        viewModelScope.launch {
            try {
                // 1️⃣ Decode image (handles HEIC, PNG, JPG automatically)
                val source = ImageDecoder.createSource(
                    context.contentResolver,
                    imageUri
                )

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(
                        context.contentResolver,
                        imageUri
                    )
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        imageUri
                    )
                }


// 2️⃣ Convert to JPEG file
                val file = File(context.cacheDir, "profile_${userId}.jpg")

                // 🔽 Resize image before upload
                val resizedBitmap = resizeBitmap(bitmap)

// 🔽 Compress harder (70%)
                file.outputStream().use { out ->
                    resizedBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        70,
                        out
                    )
                }


// 3️⃣ Create request body with CORRECT MIME
                val requestBody =
                    file.asRequestBody("image/jpeg".toMediaType())

                val imagePart =
                    MultipartBody.Part.createFormData(
                        name = "image",
                        filename = file.name,
                        body = requestBody
                    )

                val userIdBody =
                    userId.toString()
                        .toRequestBody("text/plain".toMediaType())

                val response =
                    AuthApiClient.api.uploadProfileImage(
                        image = imagePart,
                        userId = userIdBody
                    )

                if (!response.isSuccessful) {
                    throw Exception("HTTP ${response.code()}")
                }

                val body = response.body()
                    ?: throw Exception("Empty response")

                if (!body.success) {
                    throw Exception(body.message)
                }

                val imagePath = body.data?.image_url

                sessionManager?.saveProfileImageUri(imagePath)
                profileImageUrl.value =
                    AuthApiClient.IMAGE_BASE_URL + imagePath

                // ✅ SAFE LOGGING
                Log.d("UPLOAD_CODE", response.code().toString())
                Log.d("UPLOAD_SUCCESS", response.isSuccessful.toString())
                Log.d("UPLOAD_BODY", response.body()?.toString() ?: "null")
                Log.d("UPLOAD_ERROR", response.errorBody()?.string() ?: "no error")

            } catch (e: Exception) {
                uploadError.value = e.message
                e.printStackTrace()
            } finally {
                isUploading.value = false
            }
        }
    }
}
