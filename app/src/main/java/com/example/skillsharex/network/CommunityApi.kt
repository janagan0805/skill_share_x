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
data class UserResponse(
    val success: Boolean,
    val user: User?
)

data class User(
    val id: Int,
    val name: String,
    val role: String,
    val avatar_url: String?
)
data class CreatePostRequest(
    val user_id: Int,
    val title: String,
    val content: String,
    val topic: String
)

data class CreatePostResponse(
    val success: Boolean,
    val post_id: Int?
)
