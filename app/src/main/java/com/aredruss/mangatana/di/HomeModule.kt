package com.aredruss.mangatana.di

import com.aredruss.mangatana.view.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel(databaseRepository = get()) }
}
