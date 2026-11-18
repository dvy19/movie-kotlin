package com.example.movie.room


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="genre_table")
data class GenreEntity (

    @PrimaryKey
    var id:Int,

    var name:String
)