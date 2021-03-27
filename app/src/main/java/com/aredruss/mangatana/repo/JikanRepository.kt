package com.aredruss.mangatana.repo

import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class JikanRepository(
    private val jikanService: JikanService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaMapper: MediaMapper,
) {
    companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_ANIME = "anime"
    }

    suspend fun searchForMedia(type: String, title: String): Flow<ArrayList<MediaDb>> {
        return flow {
            val searchResult = jikanService.searchMedia(type, title).results
            emit(mediaMapper.mapToMediaList(searchResult))
        }.flowOn(ioDispatcher)
    }

    suspend fun getTopMediaList(type: String): Flow<List<MediaDb>> {
        return flow {
            val topMedia = jikanService.exploreMedia(type, 1).top
            emit(mediaMapper.mapToMediaList(topMedia))
        }.flowOn(ioDispatcher)
    }

    suspend fun getMedia(type: String, malId: Long): Flow<MediaResponse> {
        return flow {
            emit(jikanService.getMediaById(type, malId))
        }.flowOn(ioDispatcher)
    }
}
