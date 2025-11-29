package com.example.myapplication.features.time.data.datasource

/**
 * Fuente de datos que expone la hora real desde un backend.
 */
interface ServerTimeDataSource {
    suspend fun fetchCurrentEpochMillis(): Long
}
