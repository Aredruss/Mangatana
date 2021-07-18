package com.aredruss.mangatana.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_FILENAME)

    fun getAppState(): Flow<AppState> {
        return context.dataStore.data.map { prefs ->
            AppState(
                darkModeState = prefs[THEME_KEY] ?: 0,
                allowNsfw = prefs[AGE_KEY] ?: true
            )
        }
    }

    fun getUiMode(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: 0
        }
    }

    suspend fun updateTheme(darkModeState: Int) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = darkModeState
        }
    }

    suspend fun updateFilter(allowSmut: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[AGE_KEY] = allowSmut
        }
    }

    fun allowNsfw(): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
               prefs[AGE_KEY] ?: true
           }


    private companion object {
        private const val PREF_FILENAME = "MANGATANA_PREFS"
        private val THEME_KEY = intPreferencesKey("is_dark")
        private val AGE_KEY = booleanPreferencesKey("allow_nsfw")
    }
}
