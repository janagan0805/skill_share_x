package com.example.skillsharex.viewmodel.home

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

    // ✅ ACTIVE COURSES = status == "active"
    val activeCourses: List<CourseData>
        get() = courses.filter {
            it.status.equals("active", ignoreCase = true)
        }

    var isLoading by mutableStateOf(false)
        private set

    private var hasLoadedOnce = false

    fun loadDashboardData(force: Boolean = false) {
        if (hasLoadedOnce && !force) return

        viewModelScope.launch {
            isLoading = true
            try {
                val mentorsResponse = AuthApiClient.api.getTopMentors()
                val coursesResponse = AuthApiClient.api.getAvailableCourses()

                if (mentorsResponse.success) {
                    mentors = mentorsResponse.data ?: mentors
                }

                if (coursesResponse.success) {
                    courses = coursesResponse.data ?: courses
                }

                hasLoadedOnce = true

            } catch (e: Exception) {
                Log.e("DashboardVM", "Dashboard load failed", e)
            } finally {
                isLoading = false
            }
        }
    }
}

