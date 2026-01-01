package com.example.skillsharex.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthApiClient {

    // This should be the IP address of your computer running XAMPP
    private const val SERVER_IP = "192.168.31.11"
    private const val BASE_URL = "http://$SERVER_IP:8080/skillsharex_backend/api/"

    // Publicly accessible URL for constructing image paths
    const val IMAGE_BASE_URL = "http://$SERVER_IP:8080/skillsharex_backend/api/"


    // Added a logger to see the full request and response in Logcat
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient) // Use the client with the logger
            .build()
            .create(AuthApi::class.java)
    }
}
