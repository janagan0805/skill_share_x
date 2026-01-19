package com.example.skillsharex.viewmodel.community

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.community.*
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    var feedPosts by mutableStateOf<List<CommunityPost>>(emptyList())
        private set

    var upcomingEvents by mutableStateOf<List<UpcomingEvent>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var hasLoadedOnce = false

    // Details
    var currentPost by mutableStateOf<CommunityPost?>(null)
        private set
    var currentComments by mutableStateOf<List<Comment>>(emptyList())
        private set
    var isDetailsLoading by mutableStateOf(false)
        private set

    fun loadCommunityFeed(context: Context, force: Boolean = false) {
        if (hasLoadedOnce && !force) return

        val userId = SessionManager(context).getUserId()

        viewModelScope.launch {
            isLoading = true
            try {
                val response = AuthApiClient.api.fetchCommunityFeed(userId)

                // ✅ DO NOT CLEAR FIRST
                feedPosts = response.feedPosts ?: feedPosts
                upcomingEvents = response.upcomingEvents ?: upcomingEvents

                hasLoadedOnce = true
            } catch (_: Exception) {
                // Keep old data
            } finally {
                isLoading = false
            }
        }
    }

    fun loadPostDetails(context: Context, postId: Int) {
        val userId = SessionManager(context).getUserId()

        viewModelScope.launch {
            isDetailsLoading = true
            try {
                val response = AuthApiClient.api.getPostDetails(postId, userId)
                if (response.success) {
                    currentPost = response.post
                    currentComments = response.comments ?: emptyList()
                }
            } finally {
                isDetailsLoading = false
            }
        }
    }

    fun toggleLike(context: Context, post: CommunityPost) {
        val userId = SessionManager(context).getUserId()

        // Optimistic update
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else post.likeCount - 1

        feedPosts = feedPosts.map {
            if (it.postId == post.postId)
                it.copy(isLiked = newLiked, likeCount = newCount)
            else it
        }

        if (currentPost?.postId == post.postId) {
            currentPost = currentPost?.copy(isLiked = newLiked, likeCount = newCount)
        }

        viewModelScope.launch {
            try {
                AuthApiClient.api.toggleLike(
                    LikeRequest(userId, post.postId.toInt())
                )
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    fun addComment(context: Context, postId: Int, content: String) {
        val userId = SessionManager(context).getUserId()

        viewModelScope.launch {
            try {
                val response =
                    AuthApiClient.api.addComment(CommentRequest(userId, postId, content))
                if (response.success) {
                    loadPostDetails(context, postId)
                }
            } catch (_: Exception) {}
        }
    }
}
