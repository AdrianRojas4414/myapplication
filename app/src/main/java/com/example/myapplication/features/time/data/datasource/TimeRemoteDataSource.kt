package com.example.myapplication.features.time.data.datasource

import android.util.Log
import kotlinx.coroutines.delay

class TimeRemoteDataSource {
    companion object {
        // La hora del servidor se calcula UNA SOLA VEZ cuando se crea la primera instancia
        // Después de eso, siempre devuelve la misma hora base
        private var serverBaseTime: Long? = null
        private var serverBaseElapsedRealtime: Long? = null
    }

    /**
     * Simula obtener la hora del servidor.
     * El servidor tiene su propia hora que NO cambia cuando cambias la hora del dispositivo.
     *
     * La primera vez que se llama, "sincroniza" con el servidor.
     * Las siguientes veces, devuelve la hora del servidor calculada a partir de su tiempo base.
     */
    suspend fun getServerTime(): Result<Long> {
        return try {
            delay(500)

            if (serverBaseTime == null) {
                // Hora fija: 1 de enero de 2025, 12:00:00
                val calendar = java.util.Calendar.getInstance()
                calendar.set(2025, 0, 1, 12, 0, 0)
                serverBaseTime = calendar.timeInMillis

                Log.d("TimeRemoteDataSource", "Hora fija del servidor: $serverBaseTime")
            }

            Result.success(serverBaseTime!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Resetea el servidor (útil para testing o cuando se reinicia el dispositivo)
     */
    fun resetServer() {
        Log.d("TimeRemoteDataSource", "Reseteando servidor")
        serverBaseTime = null
        serverBaseElapsedRealtime = null
    }
}