package com.example.myapplication.features.movies.domain.model

import kotlinx.serialization.Serializable

@Serializable
class MovieModel(val title: String,
                 val posterURL: String,
                 val overview: String) {
}