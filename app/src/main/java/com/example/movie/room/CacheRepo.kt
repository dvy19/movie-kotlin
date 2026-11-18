package com.example.movie.room

class CacheRepo(
    private val genreDao: GenreDao
){

    suspend fun insertGenre(genres: List<GenreEntity>){
        genreDao.insertGenre(genres)
    }

    suspend fun getAllGenre(): List<GenreEntity>{
        return genreDao.getAllGenre()
    }

}