package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.view.media.list.LiteMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(
    val mediaDao: MediaDao
) {
    suspend fun addNewMediaEntry(media: MediaResponse, status: Int) {
        withContext(Dispatchers.IO) {
            mediaDao.insertEntry(
                MediaMapper.mapToMedia(
                    media, status
                )
            )
        }
    }

    suspend fun getSavedMedia(status: Int, type: String): ArrayList<LiteMedia> {
        return withContext(Dispatchers.IO) {
            MediaMapper.mapToLiteMediaList(
                mediaDao.getEntriesByStatus(status, type) as ArrayList
            )
        }
    }

    suspend fun clear() = mediaDao.clearDatabase()
}
