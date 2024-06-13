package com.koreatlwls.acr.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AcrDataStore(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun setAcrMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ACR_MODE_KEY] = isEnabled
        }
    }

    suspend fun getAcrMode(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[ACR_MODE_KEY] ?: false
        }.first()
    }

    fun getAcrModeFlow(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ACR_MODE_KEY] ?: false
        }
    }

    companion object {
        private const val ACR_MODE_PREF_NAME = "acrModePrefName"
        private val ACR_MODE_KEY = booleanPreferencesKey("acrModeKey")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = ACR_MODE_PREF_NAME,
        )
    }
}