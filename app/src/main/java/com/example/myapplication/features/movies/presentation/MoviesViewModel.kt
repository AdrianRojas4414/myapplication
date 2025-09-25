package com.example.myapplication.features.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.github.presentation.GirhubViewModel.GithubStateUI
import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.domain.usercases.GetMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(val useCase: GetMoviesUseCase): ViewModel() {
    sealed class MoviesUiState{
        object Init : MoviesUiState()
        object Loading : MoviesUiState()
        class Error(val message: String) : MoviesUiState()
        class Success(val movies: List<MovieModel>) : MoviesUiState()
    }

    private val _state = MutableStateFlow<MoviesUiState>(MoviesUiState.Init)
    val state : StateFlow<MoviesUiState> = _state.asStateFlow()

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MoviesUiState.Loading
            val result = useCase.invoke()

            result.fold(
                onSuccess = { list -> _state.value = MoviesUiState.Success(list) },
                onFailure = { err -> _state.value = MoviesUiState.Error(err.message ?: "Error desconocido") }
            )
        }
    }
}