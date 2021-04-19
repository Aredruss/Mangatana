package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.utils.MediaMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
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

    fun searchCategoryByName(type: String, status: Int, query: String) = flow {
        emit(mediaDao.getEntriesByQuery(type, status, query))
    }.flowOn(ioDispatcher)

    fun searchFavoriteByName(type: String, query: String) = flow {
        emit(mediaDao.getEntriesByQueryStatus(type, query))
    }.flowOn(ioDispatcher)

    suspend fun insertMediaEntry(
        media: MediaResponse,
        type: String,
        status: Int,
        isStarred: Boolean
    ) = mediaDao.upsertEntry(mediaMapper.mapToMedia(media, type, status, isStarred))

    suspend fun deleteEntry(malId: Long, type: String) = mediaDao.delete(malId, type)

    suspend fun clear() = mediaDao.clearDatabase()

    fun cancelAll() = ioDispatcher.cancel()
}
