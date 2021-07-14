package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.dao.MediaDao
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.utils.MediaMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DatabaseRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaDao: MediaDao,
    private val mediaMapper: MediaMapper
) {

    private val _update = MutableStateFlow(false)
    val requiresUpdate: Flow<Boolean> = _update

    fun getSavedMediaList(status: Int, type: String) = flow {
        emit(mediaDao.getEntriesByStatus(status, type))
    }.flowOn(ioDispatcher)

    fun getFavoriteMediaList(type: String) = flow {
        emit(mediaDao.getStarredEntries(type))
    }.flowOn(ioDispatcher)

    fun getMediaEntry(malId: Long, type: String) = flow {
        emit(mediaDao.getEntryByIdType(malId, type))
    }.flowOn(ioDispatcher)

    fun getRecentEntries(type: String) = flow {
        emit(mediaDao.getRecentEntries(type))
    }.flowOn(ioDispatcher)

    fun searchCategoryByName(type: String, status: Int, query: String) = flow {
        emit(mediaDao.getEntriesByQuery(type, status, "$query%"))
    }.flowOn(ioDispatcher)

    fun searchFavoriteByName(type: String, query: String) = flow {
        emit(mediaDao.getEntriesByQueryStatus(type, "$query%"))
    }.flowOn(ioDispatcher)

    suspend fun upsertMediaEntry(
        media: MediaResponse,
        type: String,
        status: Int,
        isStarred: Boolean
    ) = flow {
        mediaDao.insert(mediaMapper.mapToMedia(media, type, status, isStarred))
        emit {
            _update.value = true
        }
    }


    suspend fun deleteEntry(malId: Long, type: String) = mediaDao.delete(malId, type)

    suspend fun clear() = mediaDao.clearDatabase()

    fun cancelAll() = ioDispatcher.cancel()
}
