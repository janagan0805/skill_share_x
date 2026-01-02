package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.MentorDetail
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch

class MentorDetailViewModel : ViewModel() {

    var mentor by mutableStateOf<MentorDetail?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Load mentor detail from backend
     */
    fun loadMentorDetail(mentorId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = AuthApiClient.api.getMentorDetail(mentorId)

                if (response.success) {
                    mentor = response.data
                } else {
                    mentor = null
                    errorMessage = response.message
                }

            } catch (e: Exception) {
                Log.e("MentorDetailVM", "Failed to load mentor detail", e)
                mentor = null
                errorMessage = "Unable to load mentor details"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Optional: clear state when leaving screen
     */
    fun clearState() {
        mentor = null
        errorMessage = null
        isLoading = false
    }
}
