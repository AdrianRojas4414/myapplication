package com.example.myapplication.features.movies.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.features.movies.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IMovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
}