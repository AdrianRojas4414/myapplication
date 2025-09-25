package com.example.myapplication.features.movies.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.features.dollar.data.database.AppRoomDatabase
import com.example.myapplication.features.movies.data.database.dao.IMovieDao
import com.example.myapplication.features.movies.data.database.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppRoomDatabaseMovies: RoomDatabase() {
    abstract fun movieDao(): IMovieDao

    companion object {
        @Volatile
        private var Instance: AppRoomDatabaseMovies? = null

        fun getDatabase(context: Context): AppRoomDatabaseMovies {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabaseMovies::class.java,
                    "movies_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}