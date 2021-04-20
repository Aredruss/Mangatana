package com.aredruss.mangatana.di

import androidx.room.Room
import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, MediaDao.TABLE_NAME).build() }
    single { get<AppDatabase>().mediaDao() }
}
