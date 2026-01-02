package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.CourseDetailData
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {

    var courseDetail by mutableStateOf<CourseDetailData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Fetch course detail from backend using course_id
     */
    fun loadCourseDetail(courseId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = AuthApiClient.api.getCourseDetail(courseId)

                if (response.success) {
                    courseDetail = response.data
                } else {
                    errorMessage = response.message
                    courseDetail = null
                }

            } catch (e: Exception) {
                Log.e("CourseDetailVM", "Failed to load course detail", e)
                errorMessage = "Something went wrong. Please try again."
                courseDetail = null
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Optional helper to reset state (useful when leaving screen)
     */
    fun clearState() {
        courseDetail = null
        errorMessage = null
        isLoading = false
    }
}
