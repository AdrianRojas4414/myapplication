package com.example.myapplication.features.time.data.repository

import android.os.SystemClock
import android.util.Log
import com.example.myapplication.features.time.data.datasource.TimeLocalDataSource
import com.example.myapplication.features.time.data.datasource.TimeRemoteDataSource
import com.example.myapplication.features.time.domain.TimeManager
import com.example.myapplication.features.time.domain.model.SyncedTime
import com.example.myapplication.features.time.domain.repository.ITimeRepository

class TimeRepository(
    private val remoteDataSource: TimeRemoteDataSource,
    private val localDataSource: TimeLocalDataSource
) : ITimeRepository {

    override suspend fun syncTime(): Result<SyncedTime> {
        Log.d("TimeRepository", "Iniciando sincronización...")

        // Obtener hora del servidor
        return remoteDataSource.getServerTime().mapCatching { serverTime ->
            // CRÍTICO: Usar SystemClock.elapsedRealtime()
            // Este NO cambia con la hora del sistema
            val elapsedRealtime = SystemClock.elapsedRealtime()

            Log.d("TimeRepository", "Hora del servidor: $serverTime")
            Log.d("TimeRepository", "Elapsed realtime: $elapsedRealtime")

            val syncedTime = SyncedTime(
                serverTimestamp = serverTime,
                elapsedRealtimeAtSync = elapsedRealtime
            )

            // Guardar en DataStore para persistencia
            localDataSource.saveSyncedTime(syncedTime)

            // Guardar en memoria para acceso rápido
            TimeManager.setSyncedTime(syncedTime)

            Log.d("TimeRepository", "Sincronización completada: $syncedTime")

            syncedTime
        }
    }

    override suspend fun getSyncedTime(): SyncedTime? {
        // Primero intentar obtener de memoria
        if (TimeManager.hasSyncedTime()) {
            Log.d("TimeRepository", "Usando hora de memoria")
            return localDataSource.getSyncedTime()?.also {
                TimeManager.setSyncedTime(it)
            }
        }

        // Si no está en memoria, obtener de DataStore
        Log.d("TimeRepository", "Cargando hora de DataStore")
        return localDataSource.getSyncedTime()?.also {
            TimeManager.setSyncedTime(it)
        }
    }

    override suspend fun clearSyncedTime() {
        Log.d("TimeRepository", "Limpiando hora sincronizada")
        localDataSource.clearSyncedTime()
        TimeManager.clear()
    }
}