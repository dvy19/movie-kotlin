package com.example.movie.community

import com.example.movie.ApiClient
import com.example.movie.auth.SessionManager
import com.example.movie.movie.PopularMovie
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

    suspend fun getAllPosts(
        category: String?
    ): Response<PostResponse>{
        return api.getAllPosts(
            token="Bearer $token",
            category=category
        )

    }

    suspend fun getMyPosts(
        category: String?
    ): Response<List<Post>> {
        return api.getMyPosts(
            token = "Bearer $token",
            category = category
        )
    }

    suspend fun getAllSurveys(): Response<AllSurveyResponse>{
        return api.getAllSurveys(
            token="Bearer $token"
        )
    }

    suspend fun userSurveyResponse(request: List<UserSurveyResponse>): Response<List<UserSurveyResponse>>{
        return api.userSurveyRes(
            token="Bearer $token",
            body=request
        )

    }


    suspend fun likePost(
        postId: String
    ): Response<Unit> {
        return api.likePost(
            postId = postId
        )
    }



}