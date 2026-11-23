package com.example.movie.room

class CacheRepo(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao
){

    suspend fun insertGenre(genres: List<GenreEntity>){
        genreDao.insertGenre(genres)
    }

    suspend fun getAllGenre(): List<GenreEntity>{
        return genreDao.getAllGenre()
    }

    suspend fun insertMovie(
        movie_id:Int,
        image:String,
        adult: Boolean,
        title:String,
        releaseDate: String,
        originalLanguage: String,
        voteAverage: Double,
        voteCount: Int
    ){
        movieDao.insertMovie(
            MovieEntity(
                movie_id = movie_id,
                image = image,
                adult = adult,
                title = title,
                releaseDate = releaseDate,
                originalLanguage = originalLanguage,
                voteAverage = voteAverage,
                voteCount = voteCount
        )
        )
    }

    suspend fun getAllMovies():List<MovieEntity>{
        return movieDao.getAllMovies()

    }

}