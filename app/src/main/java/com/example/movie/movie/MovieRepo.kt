package com.example.movie.movie

import com.example.movie.ApiClient
import retrofit2.Response

class MovieRepo {

    val api= ApiClient.movieAPI

    suspend fun getGenre():Response <MovieGenreResponse>{

        return api.getGenre()
    }

    suspend fun getMoviesByGenre(
        genreId: Int,
        page: Int = 1,
        query: String = ""
    ): Result<MovieByGenreResponse> {
        return try {
            val response = api.getMoviesByGenre(
                genreId = genreId,
                page = page,
                query = query
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPopularMovies(
        page: Int = 1
    ): Result<PopularMovie> {
        return try {
            val response = api.getPopularMovies(
                page = page
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}