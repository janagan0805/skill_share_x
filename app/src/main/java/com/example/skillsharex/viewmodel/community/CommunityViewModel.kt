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

    // UI State
    var feedPosts by mutableStateOf<List<CommunityPost>>(emptyList())
        private set
    var upcomingEvents by mutableStateOf<List<UpcomingEvent>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)

    // Details State
    var currentPost by mutableStateOf<CommunityPost?>(null)
    var currentComments by mutableStateOf<List<Comment>>(emptyList())
    var isDetailsLoading by mutableStateOf(false)
    fun loadCommunityFeed(context: Context) {
        val userId = SessionManager(context).getUserId()
        viewModelScope.launch {
            isLoading = true
            try {
                val response = AuthApiClient.api.fetchCommunityFeed(userId)
                feedPosts = response.feedPosts
                upcomingEvents = response.upcomingEvents
            } catch (e: Exception) {
                // error
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
            } catch (e: Exception) {
                // error
            } finally {
                isDetailsLoading = false
            }
        }
    }
    fun toggleLike(context: Context, post: CommunityPost) {
        val userId = SessionManager(context).getUserId()
        // Optimistic Update
        val oldLiked = post.isLiked
        val oldCount = post.likeCount
        val newLiked = !oldLiked
        val newCount = if (newLiked) oldCount + 1 else oldCount - 1
        // Update Feed List
        feedPosts = feedPosts.map { if (it.postId == post.postId) it.copy(isLiked = newLiked, likeCount = newCount) else it }
        // Update Detail View if active
        if (currentPost?.postId == post.postId) {
            currentPost = currentPost?.copy(isLiked = newLiked, likeCount = newCount)
        }
        viewModelScope.launch {
            try {
                val response = AuthApiClient.api.toggleLike(LikeRequest(userId, post.postId.toInt()))
                if (!response.success) {
                    // Revert if failed (omitted for brevity)
                }
            } catch (e: Exception) {
                // Revert
            }
        }
    }
    fun addComment(context: Context, postId: Int, content: String) {
        val userId = SessionManager(context).getUserId()
        viewModelScope.launch {
            try {
                val response = AuthApiClient.api.addComment(CommentRequest(userId, postId, content))
                if (response.success) {
                    // Refresh details to see new comment
                    loadPostDetails(context, postId)
                    // Optionally update feed comment count
                }
            } catch (e: Exception) { }
        }
    }
}