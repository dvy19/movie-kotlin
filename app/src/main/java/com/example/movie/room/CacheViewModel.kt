package com.example.movie.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class SaveGenre{
    data class Success(val data: List<GenreEntity>): SaveGenre()
    data class Error(val message: String): SaveGenre()
    object Loading: SaveGenre()
    object Empty: SaveGenre()
}

sealed class GetCacheGenreState{
    data class Success(val data: List<GenreEntity>): GetCacheGenreState()
    data class Error(val message: String): GetCacheGenreState()
    object Loading: GetCacheGenreState()
    object Empty: GetCacheGenreState()
}


sealed class SaveMovieState{
    data class Success(val data: MovieEntity): SaveMovieState()
    data class Error(val message: String): SaveMovieState()
    object Loading: SaveMovieState()
    object Empty: SaveMovieState()
}

class CacheViewModel(
    private val repo: CacheRepo
):ViewModel(){

    private val _saveGenre = MutableStateFlow<SaveGenre>(SaveGenre.Empty)
    val saveGenre: StateFlow<SaveGenre> = _saveGenre.asStateFlow()

    private val _getGenre = MutableStateFlow<GetCacheGenreState>(GetCacheGenreState.Empty)
    val getGenre: StateFlow<GetCacheGenreState> = _getGenre.asStateFlow()


    private val _saveMovie = MutableStateFlow<SaveMovieState>(SaveMovieState.Empty)
    val saveMovie: StateFlow<SaveMovieState> = _saveMovie.asStateFlow()




    fun insertGenres(genres: List<GenreEntity>){
        viewModelScope.launch {

            try{
                val genreEntities = genres.map { genre ->
                    GenreEntity(
                        id = genre.id,
                        name = genre.name
                    )
                }

                // 2. Insert the list into the database via repository
                repo.insertGenre(genreEntities)

            }
            catch (e: Exception){
                _saveGenre.value = SaveGenre.Error(e.message ?: "An error occurred")
            }
            }
        }


    fun get_genre(){
        _saveGenre.value = SaveGenre.Loading
        viewModelScope.launch {
            try {
                val response = repo.getAllGenre()

                _getGenre.value= GetCacheGenreState.Success(response)

            } catch (e: Exception) {
                _saveGenre.value = SaveGenre.Error(e.message ?: "An error occurred")
            }

        }

    }

    fun insertMovies(
        movie_id: Int,
        image: String,
        adult: Boolean,
        title: String,
        releaseDate: String,
        originalLanguage: String,
        voteAverage: Double,
        voteCount: Int
    ) {
        viewModelScope.launch {
            _saveMovie.value = SaveMovieState.Loading
            try {

                val entity = MovieEntity(
                    movie_id = movie_id,
                    image = image,
                    adult = adult,
                    title = title,
                    releaseDate = releaseDate,
                    originalLanguage = originalLanguage,
                    voteAverage = voteAverage,
                    voteCount = voteCount
                )
                repo.insertMovie(
                    movie_id = movie_id,
                    image = image,
                    adult = adult,
                    title = title,
                    releaseDate = releaseDate,
                    originalLanguage = originalLanguage,
                    voteAverage = voteAverage,
                    voteCount = voteCount
                ) // or pass individual fields if repo takes those
                _saveMovie.value = SaveMovieState.Success(entity)


            } catch (e: Exception) {
                _saveMovie.value = SaveMovieState.Error(e.message ?: "Failed to save movie")
            }
        }
    }
    }










