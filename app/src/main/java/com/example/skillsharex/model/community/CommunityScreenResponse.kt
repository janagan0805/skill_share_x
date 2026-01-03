package com.example.skillsharex.model.community

import com.google.gson.annotations.SerializedName

data class CommunityScreenResponse(

    @SerializedName("feed_posts")
    val feedPosts: List<CommunityPost>,

    @SerializedName("upcoming_events")
    val upcomingEvents: List<UpcomingEvent>
)
