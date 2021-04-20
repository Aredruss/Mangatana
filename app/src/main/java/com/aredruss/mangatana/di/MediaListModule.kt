package com.aredruss.mangatana.di

import com.aredruss.mangatana.view.media.list.MediaListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaListModule = module {
    viewModel { MediaListViewModel(jikanRepository = get(), databaseRepository = get()) }
}
