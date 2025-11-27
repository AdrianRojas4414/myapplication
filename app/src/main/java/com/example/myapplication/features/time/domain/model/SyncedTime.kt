package com.example.myapplication.features.time.domain.model

import android.os.SystemClock
import android.util.Log

data class SyncedTime(
    val serverTimestamp: Long,           // Hora del servidor en milisegundos
    val elapsedRealtimeAtSync: Long      // SystemClock.elapsedRealtime() cuando se sincronizó
) {
    /**
     * Calcula la hora actual basándose SOLO en el tiempo transcurrido desde el boot
     * NO usa System.currentTimeMillis() en ningún momento
     */
    fun getCurrentSyncedTime(): Long {
        val currentElapsedRealtime = SystemClock.elapsedRealtime()
        val millisSinceSync = currentElapsedRealtime - elapsedRealtimeAtSync
        val result = serverTimestamp + millisSinceSync

        Log.d("SyncedTime", "=================================")
        Log.d("SyncedTime", "Server timestamp guardado: $serverTimestamp")
        Log.d("SyncedTime", "ElapsedRealtime guardado: $elapsedRealtimeAtSync")
        Log.d("SyncedTime", "ElapsedRealtime actual: $currentElapsedRealtime")
        Log.d("SyncedTime", "Millis transcurridos: $millisSinceSync")
        Log.d("SyncedTime", "Resultado: $result")
        Log.d("SyncedTime", "=================================")

        return result
    }
}