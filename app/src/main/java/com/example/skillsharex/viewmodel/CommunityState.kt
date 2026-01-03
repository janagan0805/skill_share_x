package com.example.skillsharex.viewmodel

import com.example.skillsharex.model.community.CommunityPost
import com.example.skillsharex.model.community.UpcomingEvent

data class CommunityState(

    val isLoading: Boolean = false,

    val feedPosts: List<CommunityPost> = emptyList(),

    val upcomingEvents: List<UpcomingEvent> = emptyList(),

    val errorMessage: String? = null
)
