package com.example.skillsharex.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.network.CreatePostRequest
import com.example.skillsharex.network.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class CreatePostUiState(
    val user: User? = null,
    val title: String = "",
    val description: String = "",
    val selectedTopic: String = "",
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

    // --- Derived state ---
    val isPostButtonEnabled: Boolean
        get() = uiState.title.isNotBlank()
                && uiState.description.isNotBlank()
                && uiState.selectedTopic.isNotBlank()
                && !uiState.isPosting

    // --- Load user from backend ---
    fun loadUser(userId: Int) {
        if (userId <= 0) {
            _events.tryEmit(CreatePostEvent.PostError("Invalid user session"))
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isUserLoading = true)

            try {
                val response = AuthApiClient.api.getUser(userId)

                if (response.success && response.user != null) {
                    uiState = uiState.copy(user = response.user)
                } else {
                    _events.tryEmit(CreatePostEvent.PostError("Failed to load user"))
                }

            } catch (e: Exception) {
                _events.tryEmit(
                    CreatePostEvent.PostError(
                        e.message ?: "Unable to fetch user"
                    )
                )
            } finally {
                uiState = uiState.copy(isUserLoading = false)
            }
        }
    }

    // --- Input handlers ---
    fun onTitleChange(value: String) {
        if (value.length <= 80) {
            uiState = uiState.copy(title = value)
        }
    }

    fun onDescriptionChange(value: String) {
        if (value.length <= 500) {
            uiState = uiState.copy(description = value)
        }
    }

    fun onTopicSelect(topic: String) {
        uiState = uiState.copy(selectedTopic = topic)
    }

    // --- Submit post ---
    fun submitPost() {
        val user = uiState.user
        if (user == null) {
            _events.tryEmit(CreatePostEvent.PostError("User not loaded"))
            return
        }

        if (!isPostButtonEnabled) {
            _events.tryEmit(CreatePostEvent.PostError("Fill all required fields"))
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isPosting = true)

            try {
                val response = AuthApiClient.api.createPost(
                    CreatePostRequest(
                        user_id = user.id,
                        title = uiState.title,
                        content = uiState.description,
                        topic = uiState.selectedTopic
                    )
                )

                if (response.success) {
                    _events.tryEmit(CreatePostEvent.PostSuccess)
                } else {
                    _events.tryEmit(
                        CreatePostEvent.PostError("Post creation failed")
                    )
                }

            } catch (e: Exception) {
                _events.tryEmit(
                    CreatePostEvent.PostError(
                        e.message ?: "Network error"
                    )
                )
            } finally {
                uiState = uiState.copy(isPosting = false)
            }
        }
    }
}
