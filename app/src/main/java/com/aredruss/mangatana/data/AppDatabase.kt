package com.aredruss.mangatana.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.MediaDb

@Database(entities = arrayOf(MediaDb::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}