package com.aredruss.mangatana.di

import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.utils.MediaMapper
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single { MediaMapper(defaultDispatcher = Dispatchers.Default) }
    single { JikanRepository(jikanService = get(), mediaMapper = get()) }
    single { DatabaseRepository(mediaDao = get(), mediaMapper = get()) }
}
