package com.aredruss.mangatana.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.datastore.SettingsDataStore
import com.aredruss.mangatana.repo.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val settingState = MutableStateFlow<SettingsState>(SettingsState.Empty)

    fun getSettings() = viewModelScope.launch {
        dataStore.getAppState().collect { result ->
            settingState.value = SettingsState.Success(result)
        }
    }

    fun updateTheme(isDark: Boolean) = viewModelScope.launch {
        dataStore.updateTheme(isDark)
    }

    fun updateFilter(isOn: Boolean) = viewModelScope.launch {
        dataStore.updateFilter(isOn)
    }

    fun clearDatabase() = viewModelScope.launch {
        databaseRepository.clear()
    }
}
