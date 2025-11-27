package com.example.myapplication.features.time.domain.repository

import com.example.myapplication.features.time.domain.model.SyncedTime

interface ITimeRepository {
    suspend fun syncTime(): Result<SyncedTime>
    suspend fun getSyncedTime(): SyncedTime?
    suspend fun clearSyncedTime()
}