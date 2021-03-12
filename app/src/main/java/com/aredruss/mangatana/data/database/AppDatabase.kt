package com.aredruss.mangatana.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aredruss.mangatana.data.dao.MediaDao

@Database(entities = arrayOf(MediaDb::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
