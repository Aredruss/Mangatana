package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DatabaseRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaDao: MediaDao,
    private val mediaMapper: MediaMapper
) {

    fun getSavedMediaList(status: Int, type: String) = flow {
        emit(mediaDao.getEntriesByStatus(status, type))
    }.flowOn(ioDispatcher)

    fun getFavoriteMediaList(type: String) = flow {
        emit(mediaDao.getStarredEntries(type))
    }.flowOn(ioDispatcher)

    fun getMediaEntry(malId: Long, type: String) = flow {
        emit(mediaDao.getEntryByIdType(malId, type))
    }.flowOn(ioDispatcher)

    suspend fun insertMediaEntry(media: MediaResponse, type: String, status: Int) =
        mediaDao.insertEntry(
            mediaMapper.mapToMedia(media, type, status)
        )

    suspend fun updateMediaEntry(malId: Long, status: Int, isStarred: Boolean, type: String) =
        mediaDao.updateEntry(malId, type, status, isStarred)

    suspend fun deleteMediaEntry(mediaDb: MediaDb) = mediaDao.deleteEntry(mediaDb)

    suspend fun clear() = mediaDao.clearDatabase()
}
