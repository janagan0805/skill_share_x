package com.example.skillsharex.network

import com.example.skillsharex.model.Course
import com.example.skillsharex.model.Mentor
import retrofit2.http.GET

interface DashboardApi {

    @GET("dashboard/available_courses.php")
    suspend fun getCourses(): List<Course>

    @GET("dashboard/top_mentors.php")
    suspend fun getMentors(): List<Mentor>
}
