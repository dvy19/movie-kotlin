package com.example.movie.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthInterface{

    @POST("accounts/register/")
    suspend fun register(
        @Body registerReq: RegisterReq
    ):Response<RegisterResponse>

    @POST("accounts/login/")
    suspend fun login(
        @Body loginReq: LoginReq
    ):Response<RegisterResponse>

}