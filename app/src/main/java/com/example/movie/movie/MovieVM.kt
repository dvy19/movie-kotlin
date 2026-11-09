package com.example.movie.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data class Success(val data: PopularMovie): GetPopularMoviesState()
    data class Error(val message: String): GetPopularMoviesState()
    object Loading: GetPopularMoviesState()
    object Empty: GetPopularMoviesState()
}

class MovieVM : ViewModel() {

    private val repo = MovieRepo()

    private val _genreState = MutableStateFlow<GetGenreState>(GetGenreState.Empty)
    val genreState: StateFlow<GetGenreState> = _genreState.asStateFlow()

    private val _moviesByGenreState = MutableStateFlow<GetMoviesByGenreState>(GetMoviesByGenreState.Empty)
    val moviesByGenreState: StateFlow<GetMoviesByGenreState> = _moviesByGenreState.asStateFlow()

    private val _popularMoviesState = MutableStateFlow<GetPopularMoviesState>(GetPopularMoviesState.Empty)
    val popularMoviesState: StateFlow<GetPopularMoviesState> = _popularMoviesState.asStateFlow()


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
                if (response.isSuccess) {
                    response.getOrNull()?.let {
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
}

