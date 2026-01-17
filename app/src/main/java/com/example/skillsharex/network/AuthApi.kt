package com.example.skillsharex.network

import com.example.skillsharex.data.model.SessionDetailResponse
import com.example.skillsharex.data.model.SessionListResponse
import com.example.skillsharex.data.model.UploadImageData
import com.example.skillsharex.data.models.LoginResponse
import com.example.skillsharex.model.*
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


    @GET("api/dashboard/online_mentors.php")
    suspend fun getOnlineMentors(): Response<OnlineMentorsResponse>

    @FormUrlEncoded
    @POST("api/auth/logout.php")
    suspend fun logout(
        @Field("user_id") userId: Int
    ): Response<BasicResponse>

    @GET("api/profile/get_user_courses.php")
    suspend fun getUserCourses(
        @Query("user_id") userId: Int
    ): Response<UserCoursesResponse>

    @GET("api/mentors/top_mentors.php")
    suspend fun getTopMentors(): ApiResponse<List<MentorData>>

    @GET("api/mentors/get_mentors_list.php")
    suspend fun getMentorsList(): ApiResponse<List<MentorData>>

    @GET("api/mentors/get_mentor_detail.php")
    suspend fun getMentorDetail(
        @Query("mentor_id") mentorId: Int
    ): ApiResponse<MentorDetail>


    @GET("api/dashboard/available_courses.php")
    suspend fun getAvailableCourses(): ApiResponse<List<CourseData>>

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

    @Multipart
    @POST("api/course/create_course.php")
    suspend fun createCourse(
        @Part("user_id") userId: okhttp3.RequestBody,
        @Part("title") title: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("status") status: okhttp3.RequestBody,
        @Part image: MultipartBody.Part
    ): BasicResponse




    @GET("api/user/get_user.php")
    suspend fun getUser(
        @Query("user_id") userId: Int
    ): UserResponse

    @POST("api/community/create_post.php")
    suspend fun createPost(
        @Body request: CreatePostRequest
    ): CreatePostResponse

    @GET("api/sessions/get_sessions.php")
    suspend fun getSessions(): Response<SessionListResponse>



    @GET("api/sessions/get_session_detail.php")
    suspend fun getSessionDetail(
        @Query("session_id") sessionId: Int
    ): Response<SessionDetailResponse>
}


