package com.aredruss.mangatana.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_FILENAME)

    fun getAppState(): Flow<AppState> {
        return context.dataStore.data.map { prefs ->
            AppState(
                isDark = prefs[THEME_KEY] ?: true,
                allowNsfw = prefs[AGE_KEY] ?: true
            )
        }
    }

    fun getUiMode(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: true
        }
    }

    suspend fun updateTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }

    suspend fun updateFilter(allowSmut: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[AGE_KEY] = allowSmut
        }
    }

    // Content filter is being delayed until further notice due to the fact that
    // API lacks capabilities to filter manga titles by age rating
    fun allowNsfw(): Flow<Boolean> = flow {
        context.dataStore.data.map { prefs ->
            prefs[AGE_KEY] ?: true
        }
    }

    private companion object {
        private const val PREF_FILENAME = "MANGATANA_PREFS"
        private val THEME_KEY = booleanPreferencesKey("is_dark")
        private val AGE_KEY = booleanPreferencesKey("allow_nsfw")
    }
}
