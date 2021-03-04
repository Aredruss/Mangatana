package com.aredruss.mangatana.di

import androidx.room.Room
import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.AppDatabase
import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.domain.DatabaseInteractor
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val AppModule = module {
    single { get<Retrofit>().create(JikanService::class.java) }
    single { Room.databaseBuilder(get(), AppDatabase::class.java, MediaDao.TABLE_NAME).build() }
    single { get<AppDatabase>().mediaDao() }
    single { JikanInteractor(jikanService = get()) }
    single { DatabaseInteractor(mediaDao = get()) }
    viewModel { MainViewModel(jikanInteractor = get(), databaseInteractor = get()) }
}