package com.example.skillsharex.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch

class EditCourseViewModel : ViewModel() {

    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val status = mutableStateOf("inactive")
    val imagePath = mutableStateOf<String?>(null)

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val isUpdated = mutableStateOf(false)

    private var session: SessionManager? = null

    fun initSession(context: Context) {
        session = SessionManager(context)
    }

    fun loadCourse(courseId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = AuthApiClient.api.getCourseDetail(courseId)
                if (response.success && response.data != null) {
                    val course = response.data
                    title.value = course.course_name ?: ""
                    description.value = course.description ?: ""
                    status.value = course.status
                    imagePath.value = course.cover_image
                } else {
                    errorMessage.value = "Unable to load course"
                }
            } catch (e: Exception) {
                errorMessage.value = "Something went wrong"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateCourse(courseId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val response = AuthApiClient.api.updateCourse(
                    courseId = courseId,
                    userId = session?.getUserId(),
                    title = title.value,
                    description = description.value,
                    status = status.value
                )

                if (response.status) {
                    isUpdated.value = true
                } else {
                    errorMessage.value = response.message
                }
            } catch (e: Exception) {
                errorMessage.value = "Update failed"
            } finally {
                isLoading.value = false
            }
        }
    }
}
