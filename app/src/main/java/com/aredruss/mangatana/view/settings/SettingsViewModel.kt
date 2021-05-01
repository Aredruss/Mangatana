package com.aredruss.mangatana.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.datastore.AppState
import com.aredruss.mangatana.data.datastore.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    

    fun updateAppState(
        appState: AppState
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStore.updateAppState(appState)
    }
}