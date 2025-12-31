package com.example.skillsharex.data.api

import com.example.skillsharex.data.models.LoginRequest
import com.example.skillsharex.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
