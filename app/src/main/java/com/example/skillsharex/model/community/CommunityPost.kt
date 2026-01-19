package com.example.skillsharex.model.community


import com.google.gson.annotations.SerializedName

data class CommunityPost(
    @SerializedName("post_id") val postId: String,
    @SerializedName("post_type") val postType: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar_url") val userAvatarUrl: String?,
    @SerializedName("post_title") val postTitle: String,
//    @SerializedName("post_content_snippet") val postContentSnippet: String? = null, // Used in feed
    @SerializedName("post_content") val postContent: String? = null, // Used in detail
    @SerializedName("post_image") val postImage: String?, // New: Image Path
    @SerializedName("like_count") val likeCount: Int,
    @SerializedName("comment_count") val commentCount: Int,
    @SerializedName("is_liked") val isLiked: Boolean, // New: Like Status
    @SerializedName("timestamp") val timestamp: String
)

data class Comment(
    @SerializedName("comment_id") val commentId: String,
    @SerializedName("content") val content: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_avatar_url") val userAvatarUrl: String?,
    @SerializedName("timestamp") val timestamp: String
)
data class UpcomingEvent(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("event_title") val eventTitle: String,
    @SerializedName("mentor_name") val mentorName: String,
    @SerializedName("event_date") val eventDate: String,
    @SerializedName("platform") val platform: String
)
/* ---------- RESPONSES ---------- */
data class CommunityFeedResponse(
    @SerializedName("feed_posts") val feedPosts: List<CommunityPost>,
    @SerializedName("upcoming_events") val upcomingEvents: List<UpcomingEvent>
)
data class PostDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("post") val post: CommunityPost?,
    @SerializedName("comments") val comments: List<Comment>?
)
data class GenericResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null
)
data class LikeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("is_liked") val isLiked: Boolean,
    @SerializedName("like_count") val likeCount: Int
)
data class CommentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("comment_count") val commentCount: Int
)
/* ---------- REQUESTS ---------- */
data class LikeRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("post_id") val postId: Int
)
data class CommentRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("content") val content: String
)