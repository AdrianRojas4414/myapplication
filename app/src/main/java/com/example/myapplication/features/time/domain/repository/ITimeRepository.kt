package com.example.myapplication.features.time.domain.repository

import com.example.myapplication.features.time.domain.model.ServerTime

interface ITimeRepository {
    suspend fun getCurrentTime(): Result<ServerTime>
}
