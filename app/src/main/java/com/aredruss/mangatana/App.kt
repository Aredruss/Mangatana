package com.aredruss.mangatana

import android.app.Application
import androidx.room.Room
import com.aredruss.mangatana.data.AppDatabase
import com.aredruss.mangatana.di.mangatanaModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(mangatanaModules)
        }

        appDatabase =
            Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
    }

}