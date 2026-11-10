package com.example.movie.community

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CommApiInterface {

    @POST("api/post")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Body postRequest: PostRequest
    ): Response<PostResponse>


}