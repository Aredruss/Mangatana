package com.aredruss.mangatana.repo

import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.view.media.list.LiteMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaMapper {
    suspend fun mapToMedia(media: MediaResponse, status: Int = 0): MediaDb {
        return withContext(Dispatchers.IO) {
            MediaDb(
                malId = media.malId,
                status = status,
                title = media.title,
                type = media.type.toLowerCase(),
                url = media.url,
                imageUrl = media.imageUrl,
                isStarred = false
            )
        }
    }

    suspend fun mapToMediaList(media: List<MediaResponse>): ArrayList<MediaDb> {
        return withContext(Dispatchers.IO) {
            val mediaList = media.map {
                mapToMedia(it)
            }
            return@withContext mediaList as ArrayList<MediaDb>
        }
    }

    suspend fun mapToLiteMediaList(media: List<MediaResponse>): ArrayList<LiteMedia> {
        return withContext(Dispatchers.IO) {
            val mediaList = media.map {
                mapToLiteMedia(it)
            }
            return@withContext mediaList as ArrayList<LiteMedia>
        }
    }

    suspend fun mapToLiteMediaList(media: ArrayList<MediaDb>): ArrayList<LiteMedia> {
        return withContext(Dispatchers.IO) {
            val mediaList = media.map {
                mapToLiteMedia(it)
            }
            return@withContext mediaList as ArrayList<LiteMedia>
        }
    }

    private suspend fun mapToLiteMedia(media: MediaResponse): LiteMedia {
        return withContext(Dispatchers.IO) {
            LiteMedia(
                id = media.malId,
                type = media.type,
                imageUrl = media.imageUrl,
                title = media.title
            )
        }
    }

    private suspend fun mapToLiteMedia(mediaDb: MediaDb): LiteMedia {
        return withContext(Dispatchers.IO) {
            LiteMedia(
                id = mediaDb.malId,
                type = mediaDb.type,
                imageUrl = mediaDb.imageUrl,
                title = mediaDb.title
            )
        }
    }
}
