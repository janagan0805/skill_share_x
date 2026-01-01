package com.example.skillsharex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.skillsharex.network.AuthApiClient


object AuthApiClient {

    private const val BASE_URL =
        "http://192.168.43.111/skillsharex_backend/api/"

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}
