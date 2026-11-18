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



class CacheViewModel(
    private val repo: CacheRepo
):ViewModel(){

    private val _saveGenre = MutableStateFlow<SaveGenre>(SaveGenre.Empty)
    val saveGenre: StateFlow<SaveGenre> = _saveGenre.asStateFlow()

    private val _getGenre = MutableStateFlow<GetCacheGenreState>(GetCacheGenreState.Empty)
    val getGenre: StateFlow<GetCacheGenreState> = _getGenre.asStateFlow()


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
    }









