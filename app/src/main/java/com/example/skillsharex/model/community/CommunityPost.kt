package com.example.skillsharex.model.community


import com.google.gson.annotations.SerializedName

data class CommunityPost(

    @SerializedName("post_id")
    val postId: String,

    @SerializedName("post_type")
    val postType: String,
    // expected values: "discussion", "achievement", "poll"

    @SerializedName("user_name")
    val userName: String,

    @SerializedName("user_avatar_url")
    val userAvatarUrl: String?,
    // example: "uploads/profile/jana.png" or null

    @SerializedName("post_title")
    val postTitle: String,

    @SerializedName("post_content_snippet")
    val postContentSnippet: String,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("comment_count")
    val commentCount: Int,

    @SerializedName("timestamp")
    val timestamp: String
    // ISO-8601 string, e.g. "2026-01-02T14:30:00+05:30"
)
