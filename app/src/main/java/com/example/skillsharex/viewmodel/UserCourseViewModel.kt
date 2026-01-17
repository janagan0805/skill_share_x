package com.example.skillsharex.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch


class UserCourseViewModel : ViewModel() {

    private var session: SessionManager? = null

    fun initSession(context: Context) {
        if (session == null) {
            session = SessionManager(context.applicationContext)
        }
    }
    var myCourses by mutableStateOf<List<CourseData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    val activeCourses: List<CourseData>
        get() = myCourses.filter {
            it.status.equals("active", ignoreCase = true)
        }

    val inactiveCourses: List<CourseData>
        get() = myCourses.filter {
            it.status.equals("inactive", ignoreCase = true)
        }
    fun loadMyCourses() {
        viewModelScope.launch {
            isLoading = true
            myCourses = try {
                val response =
                    AuthApiClient.api.getMyCourses(session?.getUserId() ?: "")
                if (response.success) {
                    response.data ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}
