
package com.example.skillsharex.ui.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface CommunityState {
    object Loading : CommunityState
    data class Success(val posts: List<Post>) : CommunityState
    object Error : CommunityState
    object Empty : CommunityState
}

class CommunityViewModel : ViewModel() {
    var communityState: CommunityState by mutableStateOf(CommunityState.Loading)
        private set

    var selectedFilter by mutableStateOf("All")
        private set

    fun selectFilter(filter: String) {
        selectedFilter = filter
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            communityState = CommunityState.Loading
            // Simulate network delay
            delay(1500)
            val posts = when (selectedFilter) {
                "Trending" -> samplePosts.sortedByDescending { it.likes }
                "Mentors" -> samplePosts.filter { it.userRole == "Mentor" }
                "Learners" -> samplePosts.filter { it.userRole == "Learner" }
                else -> samplePosts
            }
            communityState = if (posts.isEmpty()) CommunityState.Empty else CommunityState.Success(posts)
        }
    }

    fun toggleLike(postId: String) {
        if (communityState is CommunityState.Success) {
            val currentPosts = (communityState as CommunityState.Success).posts
            val updatedPosts = currentPosts.map {
                if (it.id == postId) {
                    it.copy(isLiked = !it.isLiked, likes = if (it.isLiked) it.likes - 1 else it.likes + 1)
                } else {
                    it
                }
            }
            communityState = CommunityState.Success(updatedPosts)
        }
    }

    fun addPost(post: Post) {
        val currentPosts = if (communityState is CommunityState.Success) {
            (communityState as CommunityState.Success).posts
        } else {
            emptyList()
        }
        val updatedPosts = listOf(post) + currentPosts
        communityState = CommunityState.Success(updatedPosts)
        // update sample posts for now
        samplePosts = updatedPosts
    }
}

private var samplePosts = listOf(
    Post(
        id = "1",
        userId = "user1",
        userName = "Jane Doe",
        userRole = "Mentor",
        userAvatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704d",
        timestamp = System.currentTimeMillis() - 7200000, // 2 hours ago
        content = "Just wrapped up a great session on Jetpack Compose! It's amazing to see how quickly you can build beautiful UIs. #AndroidDev #Compose",
        imageUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704d",
        skillTag = "Jetpack Compose",
        likes = 124,
        comments = 32
    ),
    Post(
        id = "2",
        userId = "user2",
        userName = "John Smith",
        userRole = "Learner",
        userAvatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704e",
        timestamp = System.currentTimeMillis() - 14400000, // 4 hours ago
        content = "Struggling a bit with state management in Compose. Any good resources or tips?",
        skillTag = "State Management",
        likes = 45,
        comments = 18
    ),
    Post(
        id = "3",
        userId = "user3",
        userName = "Emily White",
        userRole = "Mentor",
        userAvatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704f",
        timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
        content = "Excited to start mentoring a new batch of learners in Kotlin basics. Let's build some cool apps!",
        skillTag = "Kotlin",
        likes = 230,
        comments = 55
    )
)
