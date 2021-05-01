package com.aredruss.mangatana.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(PREF_FILENAME)

    fun getAppState(): Flow<AppState> {
        return dataStore.data.map { prefs ->
            AppState(
                isDark = prefs[THEME_KEY] ?: true,
                allowNsfw = prefs[AGE_KEY] ?: true,
                locale = prefs[LANG_KEY] ?: "en"
            )
        }
    }

    suspend fun updateAppState(appState: AppState) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = appState.isDark
            prefs[AGE_KEY] = appState.allowNsfw
            prefs[LANG_KEY] = appState.locale
        }
    }


    private companion object {
        private const val PREF_FILENAME = "MANGATANA_PREFS"

        private val THEME_KEY = booleanPreferencesKey("is_dark")
        private val LANG_KEY = stringPreferencesKey("en")
        private val AGE_KEY = booleanPreferencesKey("allow_nsfw")
    }
}