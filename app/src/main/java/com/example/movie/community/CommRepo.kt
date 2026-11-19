package com.example.movie.community

import com.example.movie.ApiClient
import okhttp3.MultipartBody
import retrofit2.Response

class CommRepo {

    val api= ApiClient.commApi

    suspend fun addPost(
        title:String,
        content:String,
        image: MultipartBody.Part?,
        category:String
    ): Response<PostResponse>{

        return api.addPost(
            token="",
            title=title,
            content=content,
            image=image,
            category=category

        )
    }
}