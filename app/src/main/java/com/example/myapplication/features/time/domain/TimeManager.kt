package com.example.myapplication.features.time.domain

import android.os.SystemClock
import android.util.Log
import com.example.myapplication.features.time.domain.model.SyncedTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton que mantiene la hora sincronizada en memoria
 * Esto asegura que solo se sincronice una vez por sesión
 */
object TimeManager {
    private var syncedTime: SyncedTime? = null
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun setSyncedTime(time: SyncedTime) {
        Log.d("TimeManager", "Configurando hora sincronizada: $time")
        syncedTime = time
        _isInitialized.value = true
    }

    fun getCurrentTime(): Long? {
        val time = syncedTime?.getCurrentSyncedTime()
        if (time == null) {
            Log.e("TimeManager", "getCurrentTime() llamado pero syncedTime es null")
        }
        return time
    }

    fun hasSyncedTime(): Boolean {
        return syncedTime != null
    }

    fun clear() {
        Log.d("TimeManager", "Limpiando hora sincronizada")
        syncedTime = null
        _isInitialized.value = false
    }

    // Para debugging
    fun getDebugInfo(): String {
        return if (syncedTime != null) {
            val time = syncedTime!!
            """
            Server Timestamp: ${time.serverTimestamp}
            Elapsed at Sync: ${time.elapsedRealtimeAtSync}
            Current Elapsed: ${SystemClock.elapsedRealtime()}
            Difference: ${SystemClock.elapsedRealtime() - time.elapsedRealtimeAtSync}
            Current Time: ${getCurrentTime()}
            """.trimIndent()
        } else {
            "No synced time available"
        }
    }
}