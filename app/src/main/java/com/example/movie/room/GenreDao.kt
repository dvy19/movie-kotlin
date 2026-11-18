package com.example.movie.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre:List <GenreEntity>)

    @Query("SELECT * FROM genre_table")
    suspend fun getAllGenre(): List<GenreEntity>


}
