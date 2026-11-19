package com.example.movie.auth

import com.example.movie.ApiClient
import retrofit2.Response

class AuthRepo(
    private val sessionManager: SessionManager
){

    val api=ApiClient.authAPI

    suspend fun register(request: RegisterReq):Response<RegisterResponse>{

        return api.register(request)
    }

    suspend fun login(request: LoginReq):Response<RegisterResponse>{

        return api.login(request)


    }
}