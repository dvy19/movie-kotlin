package com.example.movie


import com.example.movie.community.CommApiInterface
import com.example.movie.movie.MovieApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    private const val BASE_URL = "https://movie-app-backend-zvyr.onrender.com/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val movieAPI : MovieApiInterface by lazy{
        retrofit.create(MovieApiInterface::class.java)
    }

    val commApi: CommApiInterface by lazy{
        retrofit.create(CommApiInterface::class.java)
    }

}