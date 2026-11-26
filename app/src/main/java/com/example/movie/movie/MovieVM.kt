package com.example.movie.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movie.room.CacheRepo
import com.example.movie.room.CacheViewModel
import com.example.movie.room.DatabaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class GetGenreState{
    data class Success(val data: MovieGenreResponse): GetGenreState()
    data class Error(val message: String): GetGenreState()
    object Loading: GetGenreState()
    object Empty: GetGenreState()
}

sealed class GetMoviesByGenreState{
    data class Success(val data: MovieByGenreResponse): GetMoviesByGenreState()
    data class Error(val message: String): GetMoviesByGenreState()
    object Loading: GetMoviesByGenreState()
    object Empty: GetMoviesByGenreState()
}

sealed class GetPopularMoviesState{
    data class Success(val data: PopularMovieResponse): GetPopularMoviesState()
    data class Error(val message: String): GetPopularMoviesState()
    object Loading: GetPopularMoviesState()
    object Empty: GetPopularMoviesState()
}


sealed class MovieDetailState{
    object Idle : MovieDetailState()
    object Loading : MovieDetailState()
    data class Success(val data: PopularMovie) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}


class MovieVM : ViewModel() {

    private val repo = MovieRepo()

    private val _genreState = MutableStateFlow<GetGenreState>(GetGenreState.Empty)
    val genreState: StateFlow<GetGenreState> = _genreState.asStateFlow()

    private val _moviesByGenreState = MutableStateFlow<GetMoviesByGenreState>(GetMoviesByGenreState.Empty)
    val moviesByGenreState: StateFlow<GetMoviesByGenreState> = _moviesByGenreState.asStateFlow()

    private val _popularMoviesState = MutableStateFlow<GetPopularMoviesState>(GetPopularMoviesState.Empty)
    val popularMoviesState: StateFlow<GetPopularMoviesState> = _popularMoviesState.asStateFlow()

    private val _movieDetailState = MutableStateFlow<MovieDetailState>(MovieDetailState.Idle)
    val movieDetailState: StateFlow<MovieDetailState> = _movieDetailState.asStateFlow()












    fun get_movies_by_genre(
        genreId: Int,
        page: Int = 1,
        query: String = ""
    ){
        _moviesByGenreState.value = GetMoviesByGenreState.Loading
        viewModelScope.launch {
            try {
                val response = repo.getMoviesByGenre(
                    genreId = genreId,
                    page = page,
                    query = query
                )

                if (response.isSuccess) {
                    response.getOrNull()?.let {
                        _moviesByGenreState.value = GetMoviesByGenreState.Success(it)


                    } ?: throw Exception("Response body is null")
                } else {
                    throw Exception("Failed to fetch data")
                }
                } catch (e: Exception) {
                _moviesByGenreState.value = GetMoviesByGenreState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun get_genres(){

        _genreState.value = GetGenreState.Loading

        viewModelScope.launch {

            try {
                val response = repo.getGenre()

                Log.d("MovieVM genre", "Response body: ${response.body()}")
                Log.d("MovieVM genre", "Response isSuccessful: ${response.isSuccessful}")
                Log.d("MovieVM genre", "Response code: ${response.code()}")
                Log.d("MovieVM genre", "Response message: ${response.message()}")


                if (response.isSuccessful) {
                    response.body()?.let {
                        _genreState.value = GetGenreState.Success(it)


                    } ?: throw Exception("Response body is null")
                } else {
                    throw Exception("Failed to fetch data")
                }
                } catch (e: Exception) {
                _genreState.value = GetGenreState.Error(e.message ?: "An error occurred")
            }
        }



    }


    fun get_popular_movies(
        page: Int = 1
    ){
        _popularMoviesState.value = GetPopularMoviesState.Loading
        viewModelScope.launch {
            try {
                val response = repo.getPopularMovies(
                    page = page
                )
                if (response.isSuccessful && response.body()!=null) {

                    _popularMoviesState.value= GetPopularMoviesState.Success(response.body()!!)

                } else {
                    throw Exception("Failed to fetch data")
                }
                } catch (e: Exception) {
                _popularMoviesState.value = GetPopularMoviesState.Error(e.message ?: "An error occurred")
            }
        }
        }


    fun get_movie_details(
        movieId: String
    ){

        _movieDetailState.value = MovieDetailState.Loading


        viewModelScope.launch {
            try {
                val response = repo.getMovieDetails(
                    movieId = movieId
                )
                if (response.isSuccessful && response.body()!=null) {
                    _movieDetailState.value= MovieDetailState.Success(response.body()!!)
                } else {
                    throw Exception("Failed to fetch data")

                }
                } catch (e: Exception) {
                _movieDetailState.value = MovieDetailState.Error(e.message ?: "An error occurred")
            }
        }

    }

    fun search_movies(
        query: String,
        page: Int = 1
        ){
        _popularMoviesState.value = GetPopularMoviesState.Loading
        viewModelScope.launch {
            try {
                val response = repo.searchMovies(
                    query = query,
                    page = page
                )

                Log.d("MovieVM genre", "Response body: ${response.body()}")
                Log.d("MovieVM genre", "Response isSuccessful: ${response.isSuccessful}")
                Log.d("MovieVM genre", "Response code: ${response.code()}")
                Log.d("MovieVM genre", "Response message: ${response.message()}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        _popularMoviesState.value = GetPopularMoviesState.Success(it)
                    } ?: throw Exception("Response body is null")
                } else {
                    throw Exception("Failed to fetch data")
                }
            } catch (e: Exception) {
                _popularMoviesState.value = GetPopularMoviesState.Error(e.message ?: "An error occurred")
            }
        }
            }

    fun get_filter_movies(

        page: Int = 1,
        language: String = "en-US",
        sortBy: String = "popularity.desc",
        originalLanguage: String? = null,
        with_origin_country: String? = null,
        includeAdult: Boolean = false,
        releaseYear: Int? = null,
        releaseDateGte: String? = null,
        releaseDateLte: String? = null
    ) {
        _popularMoviesState.value = GetPopularMoviesState.Loading
        viewModelScope.launch {
            try {
                val response = repo.getFilterMovies(

                    page = page,
                    language = language,
                    sortBy = sortBy,
                    originalLanguage = originalLanguage?.takeIf { it.isNotBlank() },
                    with_origin_country = with_origin_country.toString(),
                    includeAdult = includeAdult,
                    releaseYear = releaseYear,
                    releaseDateGte = releaseDateGte?.takeIf { it.isNotBlank() },
                    releaseDateLte = releaseDateLte?.takeIf { it.isNotBlank() }
                )

                Log.d("MovieVM genre", "Response body: ${response.body()}")
                Log.d("MovieVM genre", "Response isSuccessful: ${response.isSuccessful}")
                Log.d("MovieVM genre", "Response code: ${response.code()}")
                Log.d("MovieVM genre", "Response message: ${response.message()}")

                if (response.isSuccessful && response.body()!=null) {
                    _popularMoviesState.value= GetPopularMoviesState.Success(response.body()!!)
                } else {
                    throw Exception("Failed to fetch data")
                }

                } catch (e: Exception) {
                _popularMoviesState.value = GetPopularMoviesState.Error(e.message ?: "An error occurred")
            }
        }
    }



}

