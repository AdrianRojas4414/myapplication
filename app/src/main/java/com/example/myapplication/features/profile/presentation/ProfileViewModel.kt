package com.example.myapplication.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.profile.domain.model.ProfileModel
import com.example.myapplication.features.profile.domain.usercases.GetProfileUseCase
import com.example.myapplication.features.time.domain.usercases.GetServerTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    val profileUseCase: GetProfileUseCase,
    private val serverTimeUseCase: GetServerTimeUseCase
): ViewModel() {
    // UI STATE
    sealed class ProfileUiState {
        object Init: ProfileUiState()
        object Loading: ProfileUiState()
        class Error(val message: String): ProfileUiState()
        class Success(val profile: ProfileModel): ProfileUiState()
    }

    sealed class TimeUiState {
        object Idle: TimeUiState()
        object Loading: TimeUiState()
        class Error(val message: String): TimeUiState()
        class Success(val formattedTime: String): TimeUiState()
    }

    // variable mutable y observable
    private var _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val state : StateFlow<ProfileUiState> = _state.asStateFlow()

    private var _timeState = MutableStateFlow<TimeUiState>(TimeUiState.Idle)
    val timeState: StateFlow<TimeUiState> = _timeState.asStateFlow()

    // evento o eventos desencadenadores
    fun showProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            val resultProfile = profileUseCase.invoke()
            resultProfile.fold(
                onSuccess = {
                    _state.value = ProfileUiState.Success(it)
                },
                onFailure = {
                    _state.value = ProfileUiState.Error(it.message.toString())
                }
            )
        }
    }

    fun fetchServerTime() {
        viewModelScope.launch(Dispatchers.IO) {
            _timeState.value = TimeUiState.Loading
            val resultTime = serverTimeUseCase.invoke()
            resultTime.fold(
                onSuccess = {
                    _timeState.value = TimeUiState.Success(it.format())
                },
                onFailure = {
                    _timeState.value = TimeUiState.Error(it.message ?: "No se pudo obtener la hora")
                }
            )
        }
    }
}
