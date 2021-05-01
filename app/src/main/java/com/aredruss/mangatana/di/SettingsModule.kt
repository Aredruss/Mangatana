package com.aredruss.mangatana.di

import com.aredruss.mangatana.data.datastore.SettingsDataStore
import com.aredruss.mangatana.view.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single { SettingsDataStore(context = androidContext()) }
    viewModel { SettingsViewModel(dataStore = get(), databaseRepository = get()) }
}
