package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.CommunityService
import kotlinx.coroutines.launch

class CreatePostViewModel : ViewModel() {

    var isSubmitting by mutableStateOf(false)
        private set

    var submitSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Create a community post
     * NOTE:
     * For Phase-2 demo, backend may not persist yet.
     * This is still production-structured.
     */
    fun createPost(
        userId: Int,
        postType: String,
        title: String,
        description: String
    ) {
        if (title.isBlank() || description.isBlank()) {
            errorMessage = "Title and description cannot be empty"
            return
        }

        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            submitSuccess = false

            try {
                // 🔹 TEMP: backend not implemented yet
                // Replace with real API when ready
                // CommunityService.api.createPost(...)

                Log.d(
                    "CreatePostVM",
                    "Create post → userId=$userId, type=$postType, title=$title"
                )

                // Simulate success for demo
                submitSuccess = true

            } catch (e: Exception) {
                Log.e("CreatePostVM", "Post creation failed", e)
                errorMessage = "Failed to create post. Try again."
            } finally {
                isSubmitting = false
            }
        }
    }

    fun resetState() {
        submitSuccess = false
        errorMessage = null
    }
}
