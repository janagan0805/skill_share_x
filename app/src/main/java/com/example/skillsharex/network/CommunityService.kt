package com.example.skillsharex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CommunityService {

    private const val BASE_URL =
        "http://192.168.31.11:8080/skillsharex_backend/"

    val api: CommunityApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommunityApi::class.java)
    }
}
