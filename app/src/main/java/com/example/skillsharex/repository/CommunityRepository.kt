package com.example.skillsharex.repository

import com.example.skillsharex.model.community.CommunityScreenResponse
import com.example.skillsharex.network.CommunityApi

class CommunityRepository(
    private val api: CommunityApi
) {

    suspend fun fetchCommunityFeed(): CommunityScreenResponse {
        return api.getCommunityFeed()
    }
}
