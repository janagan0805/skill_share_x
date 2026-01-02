package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DashboardViewModel : ViewModel() {

    var mentors by mutableStateOf<List<MentorData>>(emptyList())
        private set

    var courses by mutableStateOf<List<CourseData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading = true
            try {
                val mentorsResponse = AuthApiClient.api.getTopMentors()
                val coursesResponse = AuthApiClient.api.getAvailableCourses()

                if (mentorsResponse.success) {
                    mentors = mentorsResponse.data
                }

                if (coursesResponse.success) {
                    courses = coursesResponse.data
                }

            } catch (e: HttpException) {
                Log.e("DashboardVM", "HTTP ${e.code()} ${e.message()}")
                courses = emptyList()
                mentors = emptyList()
            }
            catch (e: Exception) {
                Log.e("DashboardVM", "Unexpected error", e)
                courses = emptyList()
                mentors = emptyList()
            }
            finally {
                isLoading = false
            }
        }
    }
}
