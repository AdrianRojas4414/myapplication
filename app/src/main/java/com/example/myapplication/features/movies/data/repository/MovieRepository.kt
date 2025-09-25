package com.example.myapplication.features.movies.data.repository

import com.example.myapplication.features.movies.data.api.dto.MoviePageDto
import com.example.myapplication.features.movies.data.datasource.MovieRemoteDataSource
import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.domain.repository.IMovieRepository

class MovieRepository(val remoteDatasource: MovieRemoteDataSource): IMovieRepository {
    override suspend fun getMovies(): Result<List<MovieModel>> {
        val response = remoteDatasource.getMovies()

        return response.fold(
            onSuccess = { pageDto ->
                val movies = pageDto.results.map {
                    MovieModel(
                        title = it.title,
                        posterURL = "https://image.tmdb.org/t/p/w185${it.poster_path}"
                    )
                }
                Result.success(movies)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}