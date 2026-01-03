package com.example.skillsharex.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.CommunityService
import com.example.skillsharex.model.community.CreatePostRequest
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

    private val api = CommunityService.api

    fun onEvent(event: CreatePostEvent) {
        when (event) {

            is CreatePostEvent.PostTypeChanged ->
                state = state.copy(postType = event.type)

            is CreatePostEvent.TitleChanged ->
                state = state.copy(title = event.title)

            is CreatePostEvent.DescriptionChanged ->
                state = state.copy(description = event.description)

            CreatePostEvent.SubmitPost ->
                submitPost()

            CreatePostEvent.ResetState ->
                state = CreatePostState()
        }
    }

    private fun submitPost() {
        viewModelScope.launch {
            state = state.copy(isSubmitting = true, errorMessage = null)

            try {
                val response = api.createPost(
                    CreatePostRequest(
                        user_id = 1, // TODO replace with SessionManager
                        post_type = state.postType,
                        skill_id = null,
                        title = state.title,
                        description = state.description
                    )
                )

                if (response.status == "success") {
                    state = state.copy(isSuccess = true)
                } else {
                    state = state.copy(errorMessage = response.message)
                }

            } catch (e: Exception) {
                state = state.copy(
                    errorMessage = e.localizedMessage ?: "Post failed"
                )
            } finally {
                state = state.copy(isSubmitting = false)
            }
        }
    }
}
