package com.example.skillsharex.ui.community

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommunityApi {

    @GET("community/get_posts.php")
    suspend fun getPosts(
        @Query("user_id") userId: Int
    ): List<Post>

    @POST("community/create_post.php")
    suspend fun createPost(
        @Body request: CreatePostRequest
    )

    @POST("community/toggle_like.php")
    suspend fun toggleLike(
        @Body request: LikeRequest
    )
}
