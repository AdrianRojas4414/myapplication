package com.example.myapplication.features.time.domain.usecases

import com.example.myapplication.features.time.domain.model.SyncedTime
import com.example.myapplication.features.time.domain.repository.ITimeRepository

class GetSyncedTimeUseCase(private val repository: ITimeRepository) {

    suspend fun invoke(): Result<SyncedTime> {
        // Primero intentar obtener hora guardada
        val savedTime = repository.getSyncedTime()

        return if (savedTime != null) {
            // Si existe, usar esa hora
            Result.success(savedTime)
        } else {
            // Si no existe, sincronizar con el servidor
            repository.syncTime()
        }
    }
}