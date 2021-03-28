package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaMapper(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend fun mapToMedia(media: MediaResponse, type: String, status: Int = 0): MediaDb {
        return withContext(defaultDispatcher) {
            MediaDb(
                malId = media.malId,
                status = status,
                title = media.title,
                type = type,
                imageUrl = media.imageUrl,
                url = media.url,
                isStarred = false
            )
        }
    }

    suspend fun mapToMediaList(media: List<MediaResponse>, type: String): ArrayList<MediaDb> {
        return withContext(defaultDispatcher) {
            val mediaList = media.map {
                mapToMedia(it, type)
            }
            return@withContext mediaList as ArrayList<MediaDb>
        }
    }
}
