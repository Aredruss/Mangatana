package com.aredruss.mangatana.di

import com.aredruss.mangatana.view.media.info.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel { DetailsViewModel(jikanRepository = get(), databaseRepository = get()) }
}
