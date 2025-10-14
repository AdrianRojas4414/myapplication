package com.example.myapplication.features.movies.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.myapplication.features.dollar.data.database.AppRoomDatabase
import com.example.myapplication.features.movies.data.database.dao.IMovieDao
import com.example.myapplication.features.movies.data.database.entity.MovieEntity

val MIGRATION_1_2 = object: Migration(1,2){
    override fun migrate(database: SQLiteConnection) {
        database.execSQL("ALTER TABLE 'movies' ADD COLUMN timestamp TEXT")
        database.execSQL("UPDATE movies SET timestamp = '' WHERE timestamp IS NULL")
    }
}

@Database(entities = [MovieEntity::class], version = 2)
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
                    //.fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}