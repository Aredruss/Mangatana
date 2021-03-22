package com.aredruss.mangatana.repo

import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JikanRepository(
    private val jikanService: JikanService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mediaMapper: MediaMapper,
) {
    companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_ANIME = "anime"
    }

    suspend fun searchForMedia(type: String, title: String): ArrayList<MediaDb> {
        return withContext(ioDispatcher) {
            mediaMapper.mapToMediaList(
                jikanService.searchMedia(type, title)
                    .results
            )
        }
    }

    suspend fun getTopMedia(type: String): ArrayList<MediaDb> {
        return withContext(ioDispatcher) {
            mediaMapper.mapToMediaList(
                jikanService.exploreMedia(type).top
            )
        }
    }

    suspend fun getMedia(type: String, malId: Long): MediaResponse {
        return withContext(ioDispatcher) {
            jikanService.getMediaById(type, malId)
        }
    }
}
