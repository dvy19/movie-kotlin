package com.example.movie.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room

@Database(
    entities = [
        GenreEntity::class,
        MovieEntity::class

    ],
    version = 2
)
abstract class MovieDatabase : androidx.room.RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao

}

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MovieDatabase? = null

    fun getDatabase(context: Context): MovieDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java, // Updated to MovieDatabase
                "movie.db"
            ).fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}