package com.example.skillsharex.ui.community

class CommunityRepository(private val api: CommunityApi) {

    suspend fun getPosts(userId: Int) =
        api.getPosts(userId)

    suspend fun createPost(userId: Int, content: String) =
        api.createPost(CreatePostRequest(userId, content))

    suspend fun toggleLike(postId: Int, userId: Int) =
        api.toggleLike(LikeRequest(postId, userId))
}
