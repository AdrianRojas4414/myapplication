package com.example.myapplication.features.time.data.repository

import com.example.myapplication.features.time.data.datasource.ServerTimeDataSource
import com.example.myapplication.features.time.domain.model.ServerTime
import com.example.myapplication.features.time.domain.repository.ITimeRepository

class ServerTimeRepository(
    private val dataSource: ServerTimeDataSource
) : ITimeRepository {
    override suspend fun getCurrentTime(): Result<ServerTime> = runCatching {
        val epochMillis = dataSource.fetchCurrentEpochMillis()
        ServerTime(epochMillis)
    }
}
