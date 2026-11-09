package com.example.movie.movie

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface{

    @GET("api/movies/get-genre/")
    suspend fun getGenre(): Response<MovieGenreResponse>

    @GET("movies/genre/{genre_id}/")
    suspend fun getMoviesByGenre(
        @Path("genre_id") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("query") query: String = ""
    ): MovieByGenreResponse

    @GET("movies/popular/")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): PopularMovie


}