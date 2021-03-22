package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaDao: MediaDao,
    private val mediaMapper: MediaMapper
) {
    suspend fun addNewMediaEntry(media: MediaResponse, status: Int) {
        withContext(ioDispatcher) {
            mediaDao.insertEntry(
                mediaMapper.mapToMedia(
                    media, status
                )
            )
        }
    }

    suspend fun getSavedMedia(status: Int, type: String): ArrayList<MediaDb> {
        return withContext(ioDispatcher) {
            mediaDao.getEntriesByStatus(status, type) as ArrayList
        }
    }

    suspend fun clear() = withContext(ioDispatcher) {
        mediaDao.clearDatabase()
    }
}
