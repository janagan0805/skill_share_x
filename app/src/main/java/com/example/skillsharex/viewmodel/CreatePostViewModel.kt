package com.example.skillsharex.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.community.CommunityPost
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class CreatePostUiState(
    val title: String = "",
    val description: String = "",
    val selectedTopic: String = "",
    val isPosting: Boolean = false
)

sealed interface CreatePostEvent {
    object PostSuccess : CreatePostEvent
    data class PostError(val message: String) : CreatePostEvent
}

class CreatePostViewModel : ViewModel() {

    var uiState by mutableStateOf(CreatePostUiState())
        private set

    private val _events = MutableSharedFlow<CreatePostEvent>()
    val events = _events.asSharedFlow()

    val isPostButtonEnabled: Boolean
        get() = uiState.title.isNotBlank()
                && uiState.description.isNotBlank()
                && uiState.selectedTopic.isNotBlank()

    fun onTitleChange(value: String) {
        uiState = uiState.copy(title = value)
    }

    fun onDescriptionChange(value: String) {
        uiState = uiState.copy(description = value)
    }

    fun onTopicSelect(topic: String) {
        uiState = uiState.copy(selectedTopic = topic)
    }

    fun submitPost(communityViewModel: CommunityViewModel) {
        if (!isPostButtonEnabled) return

        viewModelScope.launch {
            uiState = uiState.copy(isPosting = true)

            // Simulate API delay
            delay(1500)

            // ✅ MATCHES YOUR EXISTING CommunityPost MODEL
            val newPost = CommunityPost(
                postId = System.currentTimeMillis().toString(),
                userName = "Jana",
                userAvatarUrl = "https://i.pravatar.cc/150?img=12",
                postType = uiState.selectedTopic,
                postTitle = uiState.title,
                postContentSnippet = uiState.description,
                likeCount = 0,
                commentCount = 0,
                timestamp = System.currentTimeMillis().toString()
            )

            communityViewModel.addPost(newPost)

            uiState = uiState.copy(isPosting = false)
            _events.emit(CreatePostEvent.PostSuccess)
        }
    }
}
