package com.aredruss.mangatana

import android.app.Application
import androidx.room.Room
import com.aredruss.mangatana.data.database.AppDatabase
import com.aredruss.mangatana.di.mangatanaModules
import com.github.terrakok.modo.AppReducer
import com.github.terrakok.modo.Modo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    private lateinit var appDatabase: AppDatabase
    val modo = Modo(AppReducer(this))

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin {
            androidContext(this@App)
            modules(mangatanaModules)
        }

        appDatabase =
            Room.databaseBuilder(this, AppDatabase::class.java, "database").build()

        Timber.plant(Timber.DebugTree())
    }
}
