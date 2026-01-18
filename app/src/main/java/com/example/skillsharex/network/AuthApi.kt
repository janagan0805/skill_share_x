package com.example.skillsharex.network

import com.example.skillsharex.data.model.SessionDetailResponse
import com.example.skillsharex.data.model.SessionListResponse
import com.example.skillsharex.data.model.UploadImageData
import com.example.skillsharex.model.LoginResponse
import com.example.skillsharex.model.*
import com.example.skillsharex.model.community.CommentRequest
import com.example.skillsharex.model.community.CommentResponse
import com.example.skillsharex.model.community.CommunityFeedResponse
import com.example.skillsharex.model.community.CommunityScreenResponse
import com.example.skillsharex.model.community.GenericResponse
import com.example.skillsharex.model.community.LikeRequest
import com.example.skillsharex.model.community.LikeResponse
import com.example.skillsharex.model.community.PostDetailResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.GET

interface AuthApi {


    // This is authentication part -------------------------------------------

    @FormUrlEncoded
    @POST("api/auth/login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/auth/register.php")
    suspend fun register(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Response<SignUpResponse>

    @POST("api/auth/forgot_password.php")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>


    @FormUrlEncoded
    @POST("api/auth/logout.php")
    suspend fun logout(
        @Field("user_id") userId: Int
    ): Response<BasicResponse>

    // ------------------------------------------------------------------------------------------------

    // This is a Dashboard part --------------------------------

    @GET("api/dashboard/online_mentors.php")
    suspend fun getOnlineMentors(): Response<OnlineMentorsResponse>

    @GET("api/dashboard/available_courses.php")
    suspend fun getAvailableCourses(): ApiResponse<List<CourseData>>

    // ------------------------------------------------------------------------------------------------

    // This is the profile part ---------------------------------
    @GET("api/profile/get_user_courses.php")
    suspend fun getUserCourses(
        @Query("user_id") userId: Int
    ): Response<UserCoursesResponse>

    @Multipart
    @POST("api/profile/upload_profile_image.php")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Response<ApiResponse<UploadImageData>>

    @GET("api/profile/get_my_courses.php")
    suspend fun getMyCourses(
        @Query("user_id") userId: Any
    ): CourseListResponse

    @GET("api/profile/get_profile.php")
    suspend fun getProfile(
        @Query("user_id") userId: Int
    ): Response<ApiResponse<ProfileData>>


    @POST("api/profile/update_profile.php")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): BasicApiResponse

    // --------------------------------------------------------------------------------------------

    // This is the mentors part ----------------------------------------------

    @GET("api/mentors/top_mentors.php")
    suspend fun getTopMentors(): ApiResponse<List<MentorData>>

    @GET("api/mentors/get_mentors_list.php")
    suspend fun getMentorsList(): ApiResponse<List<MentorData>>

    @GET("api/mentors/get_mentor_detail.php")
    suspend fun getMentorDetail(
        @Query("mentor_id") mentorId: Int
    ): ApiResponse<MentorDetail>

    // --------------------------------------------------------------------------------------------

    // this is the Course Part ----------------------------------

    @GET("api/course/get_course_detail.php")
    suspend fun getCourseDetail(
        @Query("course_id") courseId: Int
    ): CourseDetailResponse

    @FormUrlEncoded
    @POST("api/course/update_course.php")
    suspend fun updateCourse(
        @Field("course_id") courseId: Int,
        @Field("user_id") userId: Int?,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("status") status: String
    ): BasicResponse

    @Multipart
    @POST("api/course/create_course.php")
    suspend fun createCourse(
        @Part("user_id") userId: okhttp3.RequestBody,
        @Part("title") title: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("status") status: okhttp3.RequestBody,
        @Part image: MultipartBody.Part
    ): BasicResponse

    // --------------------------------------------------------------------------------------------

    // This is User Part -------------------------------------
    @GET("api/user/get_user.php")
    suspend fun getUser(
        @Query("user_id") userId: Int
    ): UserResponse

    // ---------------------------------------------------------------------------------------------

    // This is community Part --------------------------------------

//    @GET("api/community/feed.php")
//    suspend fun getCommunityFeed(): CommunityScreenResponse
//
//    @POST("api/community/create_post.php")
//    suspend fun createPost(
//        @Body request: CreatePostRequest
//    ): CreatePostResponse

    @GET("api/community/feed.php")
    suspend fun fetchCommunityFeed(
        @Query("user_id") userId: Int // Pass user_id to check is_liked
    ): CommunityFeedResponse
    @GET("api/community/get_post_details.php")
    suspend fun getPostDetails(
        @Query("post_id") postId: Int,
        @Query("user_id") userId: Int
    ): PostDetailResponse
    @Multipart
    @POST("api/community/create_post.php")
    suspend fun createPost(
        @Part("user_id") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("topic") topic: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): GenericResponse
    @POST("api/community/toggle_like.php")
    suspend fun toggleLike(@Body request: LikeRequest): LikeResponse
    @POST("api/community/add_comment.php")
    suspend fun addComment(@Body request: CommentRequest): CommentResponse

    // --------------------------------------------------------------------------------------------------

    // This is session Part ------------------------------------------

    @GET("api/sessions/get_sessions.php")
    suspend fun getSessions(): Response<SessionListResponse>

    @GET("api/sessions/get_session_detail.php")
    suspend fun getSessionDetail(
        @Query("session_id") sessionId: Int
    ): Response<SessionDetailResponse>
}


