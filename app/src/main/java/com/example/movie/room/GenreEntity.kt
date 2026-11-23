package com.example.movie.room


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="genre_table")
data class GenreEntity (

    @PrimaryKey
    var id:Int,

    var name:String
)

@Entity(tableName = "saved_movie")
data class MovieEntity(

    @PrimaryKey
    var movie_id:Int,
    var image:String,
    var adult: Boolean,
    var title:String,
    var releaseDate: String,
    var originalLanguage: String,
    var voteAverage: Double,
    var voteCount: Int
)