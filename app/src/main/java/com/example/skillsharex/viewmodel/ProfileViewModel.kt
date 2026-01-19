    package com.example.skillsharex.viewmodel

    import android.content.Context
    import android.net.Uri
    import android.util.Log
    import android.widget.Toast
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.skillsharex.model.UpdateProfileRequest
    import com.example.skillsharex.network.AuthApiClient
    import com.example.skillsharex.utils.RefreshBus
    import com.example.skillsharex.utils.RefreshEvent
    import com.example.skillsharex.utils.SessionManager
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import okhttp3.MediaType.Companion.toMediaType
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.toRequestBody



    class ProfileViewModel : ViewModel() {

        private var sessionManager: SessionManager? = null

        fun initSession(context: Context) {
            if (sessionManager == null) {
                sessionManager = SessionManager(context.applicationContext)
            }
        }

        // ---------------- STATE ----------------

        val name = mutableStateOf("")
        val role = mutableStateOf("")

        val skills = mutableStateListOf<String>()

        val isLoading = mutableStateOf(false)
        val isUploading = mutableStateOf(false)
        val errorMessage = mutableStateOf<String?>(null)
        val imageReloadKey = mutableStateOf(0)



        // 🔑 STORE ONLY RELATIVE PATH
        val profileImagePath = mutableStateOf<String?>(null)

        // ---------------- FETCH PROFILE ----------------

        fun fetchProfile() {
            val userId = sessionManager?.getUserId() ?: return

            viewModelScope.launch {
                isLoading.value = true
                errorMessage.value = null

                try {
                    val response = AuthApiClient.api.getProfile(userId)

                    if (!response.isSuccessful) {
                        errorMessage.value = "Failed to load profile"
                        return@launch
                    }

                    val body = response.body()
                    if (body == null || !body.success || body.data == null) {
                        errorMessage.value = "Invalid profile data"
                        return@launch
                    }

                    val data = body.data

                    name.value = data.name
                    role.value = data.role

                    skills.clear()
                    data.skills?.let { skills.addAll(it) }

                    // ✅ RELATIVE PATH ONLY
                    profileImagePath.value = data.profile_image
                    data.profile_image.let {
                        sessionManager?.saveProfileImageUrl(it)
                    }

                } catch (e: Exception) {
                    errorMessage.value = "Network error"
                } finally {
                    isLoading.value = false
                }
            }
        }

        // ---------------- UPLOAD IMAGE ----------------

        fun uploadProfileImage(context: Context, imageUri: Uri) {
            val userId = sessionManager?.getUserId() ?: return

            viewModelScope.launch {
                isUploading.value = true
                errorMessage.value = null

                try {
                    val mimeType =
                        context.contentResolver.getType(imageUri) ?: "image/jpeg"

                    val inputStream =
                        context.contentResolver.openInputStream(imageUri) ?: return@launch

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
                            "profile_$userId.$extension",
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

                    if (!response.isSuccessful) {
                        errorMessage.value = "Upload failed"
                        return@launch
                    }

                    val body = response.body()
                    if (body == null || !body.success) {
                        errorMessage.value = "Upload failed"
                        return@launch
                    }

//                    if(body.success){
//                        Log.d("ProfileViewModel", "Upload success: ${body.data}")
//                        sessionManager!!.saveProfileImageUrl(body.data?.image_url)
//                        profileImagePath.value = body.data?.image_url
//                    }

                    if (body.success) {
                        profileImagePath.value = body.data?.image_url
                        sessionManager!!.saveProfileImageUrl(body.data?.image_url)
                        Toast.makeText(context, "Upload success", Toast.LENGTH_SHORT).show()

                        // 🔑 FORCE IMAGE RELOAD ONLY ON CHANGE
                        imageReloadKey.value++
                        // refresh full app
                        withContext(Dispatchers.Main) {
                            RefreshBus.send(RefreshEvent.ProfileUpdated)
                        }
                    }


                    // ✅ UPDATE IMAGE LOCALLY (NO LOOP)
    //                body.data?.image_url?.let {
    //                    profileImagePath.value = it
    //                    sessionManager?.saveProfileImageUrl(it)
    //                }

                } catch (e: Exception) {
                    errorMessage.value = "Upload error"
                } finally {
                    isUploading.value = false
                }
            }
        }

        fun saveProfileChanges(context: Context,onSuccess: () -> Unit) {
            val userId = sessionManager?.getUserId() ?: return

            viewModelScope.launch {
                isLoading.value = true
                errorMessage.value = null

                try {
                    val response = AuthApiClient.api.updateProfile(
                        UpdateProfileRequest(
                            user_id =  userId,
                            full_name = name.value.trim(),
                            role = role.value,          // fixed, not edited
                            skills =  skills.toList()
                        )
                    )

                    if (response.status != "success") {
                        errorMessage.value = "Failed to update profile"
                        return@launch
                    }else if (response.status == "success"){
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        Log.d("Success Response", response.message)
                        sessionManager?.saveUserName(name.value)
                        sessionManager?.saveSkills(skills.joinToString(","))

                        // refresh full app
                        withContext(Dispatchers.Main) {
                            RefreshBus.send(RefreshEvent.ProfileUpdated)
                        }
                    }

                    fetchProfile()
                    onSuccess()

                } catch (e: Exception) {
                    errorMessage.value = "Network error"
                } finally {
                    isLoading.value = false
                }
            }
        }

    }
