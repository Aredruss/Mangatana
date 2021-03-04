package com.aredruss.mangatana.domain

import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.data.model.MediaResponse
import com.aredruss.mangatana.ui.media.list.LiteMedia

class JikanInteractor(
    private val jikanService: JikanService
) {
    companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_ANIME = "anime"
    }

    suspend fun searchForMedia(type: String, title: String): ArrayList<MediaDb> {
        return MediaMapper.mapToMediaList(jikanService.searchMedia(type, title).results)
    }

    suspend fun getTopMedia(type: String): ArrayList<LiteMedia> {
        return MediaMapper.mapToLiteMediaList(jikanService.exploreMedia(type).top)
    }

    suspend fun getMedia(type: String, malId: Long): MediaResponse {
        return jikanService.getMedia(type, malId)
    }
}