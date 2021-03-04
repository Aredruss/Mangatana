package com.aredruss.mangatana.domain

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.MediaDb

class DatabaseInteractor(
    val mediaDao: MediaDao
) {
    suspend fun addNewMediaEntry(mediaDb: MediaDb) = mediaDao.insertEntry(mediaDb)

    suspend fun getSavedMedia(status: Int, type: String) =
        mediaDao.getEntriesByStatus(status, type) as ArrayList

    suspend fun clear() = mediaDao.clearDatabase()
}