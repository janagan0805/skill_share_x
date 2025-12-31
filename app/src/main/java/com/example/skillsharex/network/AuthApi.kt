package com.example.skillsharex.network

import com.example.skillsharex.data.models.UploadImageResponse
import com.example.skillsharex.model.BasicResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query

import com.example.skillsharex.model.LoginResponse
import com.example.skillsharex.model.OnlineMentorsResponse
import com.example.skillsharex.model.SignUpResponse
import com.example.skillsharex.model.UserCoursesResponse
import retrofit2.http.GET

interface AuthApi {

    @FormUrlEncoded
    @POST("auth/login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/register.php")
    suspend fun register(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpResponse>
    @Multipart
    @POST("auth/upload_profile_image.php")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part,
        @Query("user_id") userId: String
    ): Response<UploadImageResponse>

    @GET("dashboard/online_mentors.php")
    suspend fun getOnlineMentors(): Response<OnlineMentorsResponse>

    @FormUrlEncoded
    @POST("auth/logout.php")
    suspend fun logout(
        @Field("user_id") userId: Int
    ): Response<BasicResponse>

    @GET("profile/get_user_courses.php")
    suspend fun getUserCourses(
        @Query("user_id") userId: Int
    ): Response<UserCoursesResponse>


}
