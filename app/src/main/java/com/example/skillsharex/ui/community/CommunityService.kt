package com.example.skillsharex.ui.community

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CommunityService {

    private const val BASE_URL =
        "http://192.168.128.111/skillsharex_backend/api/"

    private val api: CommunityApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommunityApi::class.java)
    }

    val repository: CommunityRepository by lazy {
        CommunityRepository(api)
    }
}
