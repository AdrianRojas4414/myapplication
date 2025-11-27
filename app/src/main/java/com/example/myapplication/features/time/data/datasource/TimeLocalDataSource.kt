package com.example.myapplication.features.time.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.features.time.domain.model.SyncedTime
import kotlinx.coroutines.flow.first

val Context.timeDataStore by preferencesDataStore(name = "synced_time_preferences")

class TimeLocalDataSource(private val context: Context) {
    companion object {
        private val SERVER_TIMESTAMP = longPreferencesKey("server_timestamp")
        private val ELAPSED_REALTIME_AT_SYNC = longPreferencesKey("elapsed_realtime_at_sync")
    }

    suspend fun saveSyncedTime(syncedTime: SyncedTime) {
        context.timeDataStore.edit { preferences ->
            preferences[SERVER_TIMESTAMP] = syncedTime.serverTimestamp
            preferences[ELAPSED_REALTIME_AT_SYNC] = syncedTime.elapsedRealtimeAtSync
        }
    }

    suspend fun getSyncedTime(): SyncedTime? {
        val preferences = context.timeDataStore.data.first()

        val serverTimestamp = preferences[SERVER_TIMESTAMP]
        val elapsedRealtimeAtSync = preferences[ELAPSED_REALTIME_AT_SYNC]

        return if (serverTimestamp != null && elapsedRealtimeAtSync != null) {
            SyncedTime(serverTimestamp, elapsedRealtimeAtSync)
        } else {
            null
        }
    }

    suspend fun clearSyncedTime() {
        context.timeDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}