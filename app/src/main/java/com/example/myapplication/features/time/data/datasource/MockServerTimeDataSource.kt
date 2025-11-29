package com.example.myapplication.features.time.data.datasource

import android.os.SystemClock

/**
 * Simula una API remota que entrega la hora real del servidor.
 * Se apoya en elapsedRealtime para que cambios manuales en la fecha del dispositivo
 * no alteren el reloj que devolvemos a la UI.
 */
class MockServerTimeDataSource(
    private val seedEpochMillis: Long = System.currentTimeMillis(),
    private val bootReference: Long = SystemClock.elapsedRealtime()
) : ServerTimeDataSource {

    override suspend fun fetchCurrentEpochMillis(): Long {
        val elapsedSinceSeed = SystemClock.elapsedRealtime() - bootReference
        return seedEpochMillis + elapsedSinceSeed
    }
}
