package com.example.movie.auth

import android.util.Log
import com.example.movie.ApiClient
import okhttp3.MultipartBody
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

    suspend fun createUser(
        name:String,
        gender:String,
        city:String,
        profile: MultipartBody.Part?
    ):Response<USerProfileRes>{
        val token="Bearer ${sessionManager.getAccessToken()}"
        Log.d("m",token)
        return api.createUser(
            token=token,
            name=name,
            gender=gender,
            city=city,
            profile=profile
        )
    }


}