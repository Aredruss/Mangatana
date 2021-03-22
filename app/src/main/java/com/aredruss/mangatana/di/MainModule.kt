package com.aredruss.mangatana.di

import androidx.room.Room
import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.AppDatabase
import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.repo.MediaMapper
import com.aredruss.mangatana.view.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val AppModule = module {
    single { MediaMapper(Dispatchers.Default) }
    single { get<Retrofit>().create(JikanService::class.java) }
    single { Room.databaseBuilder(get(), AppDatabase::class.java, MediaDao.TABLE_NAME).build() }
    single { get<AppDatabase>().mediaDao() }
    single { JikanRepository(jikanService = get(), mediaMapper = get()) }
    single { DatabaseRepository(mediaDao = get(), mediaMapper = get()) }
    viewModel { MainViewModel(jikanRepository = get(), databaseRepository = get()) }
}
