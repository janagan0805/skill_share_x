package com.example.skillsharex.data.api

import com.example.skillsharex.model.LoginRequest
import com.example.skillsharex.model.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("login") // Change this to your actual endpoint
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

object RetrofitClient {
    // Change this to your actual base URL
    private const val BASE_URL = "http://192.168.31.11:8080/skillsharex_backend/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ✅ CORRECT TYPE
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
}