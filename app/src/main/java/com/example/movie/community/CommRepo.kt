package com.example.movie.community

import com.example.movie.ApiClient
import retrofit2.Response

class CommRepo {

    val api= ApiClient.commApi

    suspend fun addPost(request: PostRequest): Response<PostResponse>{

        return api.addPost(
            token="",
            request
        )
    }
}