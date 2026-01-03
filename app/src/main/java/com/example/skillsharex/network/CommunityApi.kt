package com.example.skillsharex.network

import com.example.skillsharex.model.community.CommunityScreenResponse
import retrofit2.http.GET

interface CommunityApi {

    @GET("api/community/feed.php")
    suspend fun getCommunityFeed(): CommunityScreenResponse
}
