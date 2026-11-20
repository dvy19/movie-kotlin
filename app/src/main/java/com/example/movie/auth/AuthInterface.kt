package com.example.movie.auth

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthInterface{

    @POST("accounts/register/")
    suspend fun register(
        @Body registerReq: RegisterReq
    ):Response<RegisterResponse>

    @POST("accounts/login/")
    suspend fun login(
        @Body loginReq: LoginReq
    ):Response<RegisterResponse>

    @POST("accounts/create-user/")
    suspend fun createUser(
        @Header("Authorization") token: String,

        @Part("name") name:String,
        @Part("gender") gender:String,
        @Part("city") city:String,
        @Part profile: MultipartBody.Part?

    ):Response<USerProfileRes>



}