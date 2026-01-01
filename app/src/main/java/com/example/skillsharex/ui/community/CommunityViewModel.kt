package com.example.skillsharex.ui.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    // ✅ NO-ARG ViewModel (CRASH FIXED)

    private val userId = 1   // later replace with SessionManager

    var posts: List<Post> by mutableStateOf(emptyList())
        private set

    fun loadPosts() {
        viewModelScope.launch {
            try {
                posts = CommunityService.repository.getPosts(userId)
            } catch (e: Exception) {
                posts = emptyList() // 🚑 prevent crash
            }
        }
    }


    fun createPost(content: String) {
        viewModelScope.launch {
            CommunityService.repository.createPost(userId, content)
            loadPosts()
        }
    }

    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            CommunityService.repository.toggleLike(postId, userId)
            loadPosts()
        }
    }
}
