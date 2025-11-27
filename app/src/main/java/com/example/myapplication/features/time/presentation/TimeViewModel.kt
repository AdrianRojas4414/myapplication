package com.example.myapplication.features.time.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.time.domain.TimeManager
import com.example.myapplication.features.time.domain.usecases.GetSyncedTimeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeViewModel(
    private val getSyncedTimeUseCase: GetSyncedTimeUseCase
) : ViewModel() {

    private val _currentTime = MutableStateFlow("")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _debugInfo = MutableStateFlow("")
    val debugInfo: StateFlow<String> = _debugInfo.asStateFlow()

    init {
        Log.d("TimeViewModel", "Inicializando TimeViewModel")
        initializeTime()
    }

    private fun initializeTime() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = getSyncedTimeUseCase.invoke()

                result.fold(
                    onSuccess = { time ->
                        Log.d("TimeViewModel", "Hora sincronizada exitosamente: $time")
                        startTimeTicker()
                    },
                    onFailure = { error ->
                        Log.e("TimeViewModel", "Error al sincronizar hora", error)
                        _currentTime.value = "Error: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                Log.e("TimeViewModel", "Excepción en initializeTime", e)
                _currentTime.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startTimeTicker() {
        Log.d("TimeViewModel", "Iniciando ticker de tiempo")
        viewModelScope.launch {
            var tickCount = 0
            while (isActive) {
                try {
                    // Obtener hora del TimeManager
                    val currentTimeMillis = TimeManager.getCurrentTime()

                    if (currentTimeMillis != null) {
                        _currentTime.value = formatTime(currentTimeMillis)

                        // Log cada 10 segundos para debugging
                        if (tickCount % 10 == 0) {
                            Log.d("TimeViewModel", "Tick $tickCount - Hora: ${_currentTime.value}")
                            _debugInfo.value = TimeManager.getDebugInfo()
                        }
                        tickCount++
                    } else {
                        Log.e("TimeViewModel", "TimeManager.getCurrentTime() retornó null")
                        _currentTime.value = "Error: No time available"
                    }

                    delay(1000) // Actualizar cada segundo
                } catch (e: Exception) {
                    Log.e("TimeViewModel", "Error en ticker", e)
                    _currentTime.value = "Error: ${e.message}"
                    delay(1000)
                }
            }
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return formatter.format(Date(timeMillis))
    }

    fun formatTimeWithDate(timeMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(Date(timeMillis))
    }

    fun getDebugInfo(): String {
        return TimeManager.getDebugInfo()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TimeViewModel", "TimeViewModel cleared")
    }
}