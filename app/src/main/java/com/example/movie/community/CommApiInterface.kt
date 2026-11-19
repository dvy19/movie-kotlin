package com.example.movie.community

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CommApiInterface {

    @Multipart
    @POST("api/movies/post")
    suspend fun addPost(
        @Header("Authorization") token: String,

        @Part("title") title: String,
        @Part("content") content: String,
        @Part("category") category: String,
        @Part image:MultipartBody.Part?

    ): Response<PostResponse>


}