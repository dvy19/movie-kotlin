package com.example.movie.community

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommApiInterface {

    @Multipart
    @POST("movies/posts")
    suspend fun addPost(
        @Header("Authorization") token: String,

        @Part("title") title: String,
        @Part("content") content: String,
        @Part("category") category: String,
        @Part image:MultipartBody.Part?

    ): Response<PostResponse>

    @GET("movies/posts/")
    suspend fun getAllPosts(
        @Header("Authorization") token: String,
        //@Query("page") page: Int,
        @Query("category") category: String?=null
    ) : Response<PostResponse>

    @GET("movies/posts/me")
    suspend fun getMyPosts(
        @Header("Authorization") token: String,
        //@Query("page") page: Int,
        @Query("category") category: String?=null
    ) : Response<List<Post>>

    @GET("accounts/surveys/create/")
    suspend fun getAllSurveys(
        @Header("Authorization") token: String,
    ) : Response<AllSurveyResponse>

    @POST("accounts/surveys/submit-survey-response/")
    suspend fun userSurveyRes(
        @Header("Authorization") token: String,
        @Body body: List<UserSurveyResponse>
    ): Response<List<UserSurveyResponse>>


    @POST("movies/posts/like/{post_id}")
    suspend fun likePost(
        @Path("post_id") postId: String
    ): Response<Unit>

    @POST("chats/create-room/")
    suspend fun createChatRoom(
        @Header("Authorization") token: String,
        @Body body: CreateRoomReq
    ): Response<CreateRoomRes>

    @GET("chats/create-room/")
    suspend fun getChatRooms(
        @Header("Authorization") token: String,
    ): Response<List<InboxRes>>










}