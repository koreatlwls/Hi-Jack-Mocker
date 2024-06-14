package com.koreatlwls.hjm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class HjmDataStore(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun setHjmMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[HJM_MODE_KEY] = isEnabled
        }
    }

    suspend fun getHjmMode(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[HJM_MODE_KEY] ?: false
        }.first()
    }

    fun getHjmModeFlow(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[HJM_MODE_KEY] ?: false
        }
    }

    companion object {
        private const val HJM_MODE_PREF_NAME = "hjmModePrefName"
        private val HJM_MODE_KEY = booleanPreferencesKey("hjmModeKey")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = HJM_MODE_PREF_NAME,
        )
    }
}