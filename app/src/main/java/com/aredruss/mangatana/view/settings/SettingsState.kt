package com.aredruss.mangatana.view.settings

import com.aredruss.mangatana.data.datastore.AppState

sealed class SettingsState {
    object Empty : SettingsState()
    class Success(val appState: AppState) : SettingsState()
}
