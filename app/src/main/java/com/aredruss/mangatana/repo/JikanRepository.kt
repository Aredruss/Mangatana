package com.aredruss.mangatana.repo

import com.aredruss.mangatana.api.JikanService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class JikanRepository(
    private val jikanService: JikanService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaMapper: MediaMapper,
) {

    suspend fun searchForMedia(type: String, title: String) = flow {
        emit(
            mediaMapper.mapToMediaList(
                media = jikanService.searchMedia(type, title).results,
                type = type
            )
        )
    }.flowOn(ioDispatcher)

    suspend fun getTopMediaList(type: String) = flow {
        emit(
            mediaMapper.mapToMediaList(
                media = jikanService.exploreMedia(type, 1).top,
                type = type
            )
        )
    }.flowOn(ioDispatcher)

    suspend fun getMedia(type: String, malId: Long) = flow {
        emit(jikanService.getMediaById(type, malId))
    }.flowOn(ioDispatcher)

    fun cancelAll() = ioDispatcher.cancel()

    companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_ANIME = "anime"
    }
}
