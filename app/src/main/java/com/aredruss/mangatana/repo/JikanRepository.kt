package com.aredruss.mangatana.repo

import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.utils.MediaMapper
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
        kotlinx.coroutines.delay(DEBOUNCE_TIME)
        emit(
            mediaMapper.mapToMediaList(
                media = jikanService.searchByTitle(type, title).results.apply {
                                                                              
                },
                type = type
            )
        )
    }.flowOn(ioDispatcher)

    suspend fun getTopMediaList(type: String) = flow {
        kotlinx.coroutines.delay(DEBOUNCE_TIME)
        emit(
            mediaMapper.mapToMediaList(
                media = jikanService.getTopMedia(type, 1).top,
                type = type
            )
        )
    }.flowOn(ioDispatcher)

    suspend fun getMedia(type: String, malId: Long) = flow {
        kotlinx.coroutines.delay(DEBOUNCE_TIME)
        emit(jikanService.getMediaById(type, malId))
    }.flowOn(ioDispatcher)

    fun cancelAll() = ioDispatcher.cancel()

    companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_ANIME = "anime"
        const val DEBOUNCE_TIME = 500L
    }
}
