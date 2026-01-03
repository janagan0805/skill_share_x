package com.example.skillsharex.network

import com.example.skillsharex.model.community.CommunityPostResponse
import com.example.skillsharex.model.community.CommunityScreenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunityApi {

    @GET("api/community/feed.php")
    suspend fun getCommunityFeed(): CommunityScreenResponse

    @POST("api/community/post.php")
    suspend fun createPost(
        @Body body: com.example.skillsharex.model.community.CreatePostRequest
    ): CommunityPostResponse
}
