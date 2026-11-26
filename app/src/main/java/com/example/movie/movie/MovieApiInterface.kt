package com.example.movie.movie

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface{

    @GET("movies/get-genre/")
    suspend fun getGenre(): Response<MovieGenreResponse>

    @GET("/movies/genre/{genre_id}/")
    suspend fun getMoviesByGenre(
        @Path("genre_id") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("query") query: String = ""
    ): MovieByGenreResponse

    @GET("movies/popular/")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): Response<PopularMovieResponse>

    @GET("/movies/{movie_id}/")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: String
    ): Response<PopularMovie>

    @GET("movies/search/")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Response<PopularMovieResponse>

    @GET("movies/filter-movies/")
    suspend fun getFilterMovies(

        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_original_language") originalLanguage: String? = null,
        @Query("with_origin_country") with_origin_country: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("primary_release_year") releaseYear: Int? = null,
        @Query("primary_release_date.gte") releaseDateGte: String? = null,
        @Query("primary_release_date.lte") releaseDateLte: String? = null
    ) : Response<PopularMovieResponse>



}