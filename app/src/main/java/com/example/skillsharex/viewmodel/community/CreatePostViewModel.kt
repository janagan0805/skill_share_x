package com.example.skillsharex.viewmodel.community
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.User
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
data class CreatePostUiState(
    val user: User? = null,
    val title: String = "",
    val description: String = "",
    val selectedTopic: String = "",
    val selectedImageUri: Uri? = null, // New
    val isPosting: Boolean = false,
    val isUserLoading: Boolean = false
)
sealed interface CreatePostEvent {
    object PostSuccess : CreatePostEvent
    data class PostError(val message: String) : CreatePostEvent
}
class CreatePostViewModel : ViewModel() {
    var uiState by mutableStateOf(CreatePostUiState())
        private set
    private val _events = MutableSharedFlow<CreatePostEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()
    val isPostButtonEnabled: Boolean
        get() = uiState.title.isNotBlank() &&
                uiState.description.isNotBlank() &&
                uiState.selectedTopic.isNotBlank() &&
                !uiState.isPosting
    fun loadUser(userId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isUserLoading = true)
            try {
                val response = AuthApiClient.api.getUser(userId)
                if (response.success && response.user != null) {
                    uiState = uiState.copy(user = response.user)
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                uiState = uiState.copy(isUserLoading = false)
            }
        }
    }
    fun onTitleChange(value: String) { if (value.length <= 80) uiState = uiState.copy(title = value) }
    fun onDescriptionChange(value: String) { if (value.length <= 500) uiState = uiState.copy(description = value) }
    fun onTopicSelect(topic: String) { uiState = uiState.copy(selectedTopic = topic) }

    // New: Handle Image Selection
    fun onImageSelected(uri: Uri?) {
        uiState = uiState.copy(selectedImageUri = uri)
    }
    fun submitPost(context: Context) {
        val user = uiState.user ?: return
        if (!isPostButtonEnabled) return
        viewModelScope.launch {
            uiState = uiState.copy(isPosting = true)
            try {
                // Convert simple types to RequestBody
                val userIdPart = user.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val titlePart = uiState.title.toRequestBody("text/plain".toMediaTypeOrNull())
                val contentPart = uiState.description.toRequestBody("text/plain".toMediaTypeOrNull())
                val topicPart = uiState.selectedTopic.toRequestBody("text/plain".toMediaTypeOrNull())
                var imagePart: MultipartBody.Part? = null

                // Process Image
                uiState.selectedImageUri?.let { uri ->
                    val file = getFileFromUri(context, uri)
                    if (file != null) {
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    }
                }
                // API Call
                val response = AuthApiClient.api.createPost(
                    userId = userIdPart,
                    title = titlePart,
                    content = contentPart,
                    topic = topicPart,
                    image = imagePart
                )
                if (response.success) {
                    _events.tryEmit(CreatePostEvent.PostSuccess)
                } else {
                    _events.tryEmit(CreatePostEvent.PostError(response.message ?: "Failed"))
                }
            } catch (e: Exception) {
                _events.tryEmit(CreatePostEvent.PostError(e.message ?: "Error"))
            } finally {
                uiState = uiState.copy(isPosting = false)
            }
        }
    }
    // Helper to get file from URI (Put this in a Utils class ideally)
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileName(context, uri)
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun getFileName(context: Context, uri: Uri): String {
        var name = "temp_image"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) name = it.getString(index)
            }
        }
        return name
    }
}