package com.example.skillsharex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "http://192.168.43.111/skillsharex_backend/api/sessions/"

    val sessionApi: SessionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SessionApi::class.java)
    }
}
