
package com.example.skillsharex.ui.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class CreatePostUiState(
    val title: String = "",
    val description: String = "",
    val selectedTopic: String = "",
    val imageUri: String? = null,
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
        get() = uiState.title.isNotBlank() && uiState.description.isNotBlank() && uiState.selectedTopic.isNotBlank()

    fun onTitleChange(newTitle: String) {
        if (newTitle.length <= 80) {
            uiState = uiState.copy(title = newTitle)
        }
    }

    fun onDescriptionChange(newDescription: String) {
        if (newDescription.length <= 500) {
            uiState = uiState.copy(description = newDescription)
        }
    }

    fun onTopicSelect(topic: String) {
        uiState = uiState.copy(selectedTopic = topic)
    }

    fun submitPost(communityViewModel: CommunityViewModel) {
        if (!isPostButtonEnabled) return

        viewModelScope.launch {
            uiState = uiState.copy(isPosting = true)
            // Simulate network request
            delay(2000)

            // In a real app, you would get the user details from a session manager or repository
            val newPost = Post(
                id = System.currentTimeMillis().toString(),
                userId = "currentUser",
                userName = "John Appleseed", // Replace with actual user name
                userRole = "Mentor",          // Replace with actual user role
                userAvatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704d", // Replace with actual avatar
                timestamp = System.currentTimeMillis(),
                content = uiState.description,
                imageUrl = uiState.imageUri,
                skillTag = uiState.selectedTopic,
                likes = 0,
                comments = 0,
                isLiked = false
            )

            // Add post to the community feed via the shared ViewModel
            communityViewModel.addPost(newPost)

            uiState = uiState.copy(isPosting = false)
            _events.emit(CreatePostEvent.PostSuccess)
        }
    }
}

