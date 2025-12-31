package com.example.skillsharex.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

data class UploadResponse(
    val status: Boolean,
    val message: String,
    val data: ImageData?
)

data class ImageData(
    val image_url: String
)

interface ApiService {

    @Multipart
    @POST("api/upload_profile_image.php")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part,
        @Query("user_id") userId: Int
    ): Response<UploadResponse>
}
