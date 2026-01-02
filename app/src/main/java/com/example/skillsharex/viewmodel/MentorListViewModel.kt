package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch

class MentorListViewModel : ViewModel() {

    var mentors by mutableStateOf<List<MentorData>>(emptyList())

        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Fetch top mentors from backend
     */
    fun loadMentors() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = AuthApiClient.api.getTopMentors()

                if (response.success) {
                    mentors = response.data
                } else {
                    mentors = emptyList()
                    errorMessage = response.message
                }

            } catch (e: Exception) {
                Log.e("MentorListVM", "Failed to load mentors", e)
                mentors = emptyList()
                errorMessage = "Unable to load mentors"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Optional: clear state when leaving screen
     */
    fun clearState() {
        mentors = emptyList()
        errorMessage = null
        isLoading = false
    }
}
