package com.example.movie.community

import com.example.movie.ApiClient
import com.example.movie.auth.SessionManager
import okhttp3.MultipartBody
import retrofit2.Response

class CommRepo(
    private val sessionManager: SessionManager
) {

    val api= ApiClient.commApi

    val token=sessionManager.getAccessToken()




    suspend fun addPost(
        title:String,
        content:String,
        image: MultipartBody.Part?,
        category:String
    ): Response<PostResponse>{


        return api.addPost(
            token="Bearer $token",
            title=title,
            content=content,
            image=image,
            category=category

        )
    }
}