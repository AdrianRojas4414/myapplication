package com.example.myapplication.features.time.data.datasource

import android.os.SystemClock
import com.example.myapplication.features.time.data.local.ServerTimeLocalStore
import com.example.myapplication.features.time.data.local.ServerTimeReference
import kotlin.math.max

/**
 * Fuente mock que:
 * 1) Usa una hora fija definida en cГіdigo como "hora real" del servidor.
 * 2) Hace avanzar el reloj con elapsedRealtime (no depende de la hora del sistema).
 * 3) Persiste la referencia para sobrevivir reinicios sin internet.
 */
class PersistentMockServerTimeDataSource(
    private val localStore: ServerTimeLocalStore,
    private val manualSeedEpochMillis: Long
) : ServerTimeDataSource {

    override suspend fun fetchCurrentEpochMillis(): Long {
        val nowElapsed = SystemClock.elapsedRealtime()
        val nowWallClock = System.currentTimeMillis()

        // Cargar referencia guardada o inicializar con la hora fija configurada.
        val storedRef = localStore.getReference() ?: ServerTimeReference(
            referenceEpochMillis = manualSeedEpochMillis,
            referenceElapsedMillis = nowElapsed,
            referenceWallClockMillis = nowWallClock
        ).also { localStore.save(it) }

        val deltaElapsed = nowElapsed - storedRef.referenceElapsedMillis
        val delta = if (deltaElapsed >= 0) {
            // Mismo boot: usar reloj monГіtono.
            deltaElapsed
        } else {
            // Boot nuevo: estimar con wall clock (clamp para evitar retroceso por cambios manuales).
            max(0, nowWallClock - storedRef.referenceWallClockMillis)
        }
        val currentEpoch = storedRef.referenceEpochMillis + delta

        // Guardar la nueva referencia para que continГєe avanzando en siguientes lecturas y reinicios.
        localStore.save(
            ServerTimeReference(
                referenceEpochMillis = currentEpoch,
                referenceElapsedMillis = nowElapsed,
                referenceWallClockMillis = nowWallClock
            )
        )

        return currentEpoch
    }
}
