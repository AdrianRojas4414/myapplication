package com.example.myapplication.features.time.data.local

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first

data class ServerTimeReference(
    val referenceEpochMillis: Long,
    val referenceElapsedMillis: Long,
    val referenceWallClockMillis: Long
)

class ServerTimeLocalStore(
    context: Context
) {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("server_time.preferences_pb") }
    )

    private val epochKey = longPreferencesKey("epoch_at_reference")
    private val elapsedKey = longPreferencesKey("elapsed_at_reference")
    private val wallClockKey = longPreferencesKey("wallclock_at_reference")

    suspend fun getReference(): ServerTimeReference? {
        val preferences = dataStore.data.first()
        val epoch = preferences[epochKey] ?: return null
        val elapsed = preferences[elapsedKey] ?: return null
        val wallClock = preferences[wallClockKey] ?: return null
        return ServerTimeReference(epoch, elapsed, wallClock)
    }

    suspend fun save(reference: ServerTimeReference) {
        dataStore.edit { prefs ->
            prefs[epochKey] = reference.referenceEpochMillis
            prefs[elapsedKey] = reference.referenceElapsedMillis
            prefs[wallClockKey] = reference.referenceWallClockMillis
        }
    }
}
