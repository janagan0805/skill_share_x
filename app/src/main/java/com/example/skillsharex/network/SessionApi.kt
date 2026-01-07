package com.example.skillsharex.network

import com.example.skillsharex.data.model.Session
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SessionApi {

    @GET("get_sessions.php")
    suspend fun getSessions(): Response<List<Session>>

    @GET("get_session_detail.php")
    suspend fun getSessionDetail(
        @Query("session_id") sessionId: Int
    ): Response<Session>
}
