package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/* ---------------- EVENTS ---------------- */

sealed class CreatePostEvent {

    data class PostTypeChanged(val type: String) : CreatePostEvent()
    data class TitleChanged(val title: String) : CreatePostEvent()
    data class DescriptionChanged(val description: String) : CreatePostEvent()

    object SubmitPost : CreatePostEvent()
    object ResetState : CreatePostEvent()
}

/* ---------------- UI STATE ---------------- */

data class CreatePostState(
    val postType: String = "discussion",
    val title: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

/* ---------------- VIEWMODEL ---------------- */

class CreatePostEventViewModel : ViewModel() {

    var state by mutableStateOf(CreatePostState())
        private set

    fun onEvent(event: CreatePostEvent) {
        when (event) {

            is CreatePostEvent.PostTypeChanged -> {
                state = state.copy(postType = event.type)
            }

            is CreatePostEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }

            is CreatePostEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }

            CreatePostEvent.SubmitPost -> {
                submitPost()
            }

            CreatePostEvent.ResetState -> {
                state = CreatePostState()
            }
        }
    }

    /* ---------------- SUBMIT LOGIC ---------------- */

    private fun submitPost() {
        if (state.title.isBlank() || state.description.isBlank()) {
            state = state.copy(errorMessage = "Title and description cannot be empty")
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isSubmitting = true,
                errorMessage = null,
                isSuccess = false
            )

            try {
                // 🔹 Phase-2: backend not wired yet
                // TODO (Phase-3):
                // CommunityService.api.createPost(
                //   userId = ...
                //   postType = state.postType,
                //   title = state.title,
                //   description = state.description
                // )

                Log.d(
                    "CreatePostEventVM",
                    "Submitting post → type=${state.postType}, title=${state.title}"
                )

                // Demo success
                state = state.copy(
                    isSubmitting = false,
                    isSuccess = true
                )

            } catch (e: Exception) {
                Log.e("CreatePostEventVM", "Create post failed", e)
                state = state.copy(
                    isSubmitting = false,
                    errorMessage = "Failed to create post"
                )
            }
        }
    }
}
