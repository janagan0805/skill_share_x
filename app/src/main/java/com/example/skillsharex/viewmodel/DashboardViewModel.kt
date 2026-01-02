package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    var courses: List<CourseData> = emptyList()
        private set

    var mentors: List<MentorData> = emptyList()
        private set

    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                // ✅ Courses
                val courseResponse = AuthApiClient.api.getAvailableCourses()
                if (courseResponse.isSuccessful) {
                    courses = courseResponse.body()?.data ?: emptyList()
                }

                // ✅ Mentors
                val mentorResponse = AuthApiClient.api.getTopMentors()
                if (mentorResponse.isSuccessful) {
                    mentors = mentorResponse.body()?.data ?: emptyList()
                }

            } catch (e: Exception) {
                Log.e("DASHBOARD_VM", "Error loading dashboard", e)
            }
        }
    }
}
