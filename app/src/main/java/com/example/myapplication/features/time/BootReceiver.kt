package com.example.myapplication.features.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myapplication.features.time.data.datasource.TimeLocalDataSource
import com.example.myapplication.features.time.data.datasource.TimeRemoteDataSource
import com.example.myapplication.features.time.domain.TimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Dispositivo reiniciado - limpiando hora sincronizada")

            CoroutineScope(Dispatchers.IO).launch {
                // Limpiar DataStore
                val dataSource = TimeLocalDataSource(context)
                dataSource.clearSyncedTime()

                // Limpiar memoria
                TimeManager.clear()

                // Resetear servidor simulado
                TimeRemoteDataSource().resetServer()

                Log.d("BootReceiver", "Limpieza completada")
            }
        }
    }
}