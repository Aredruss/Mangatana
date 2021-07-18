package com.aredruss.mangatana.repo

import androidx.preference.PreferenceDataStore
import com.aredruss.mangatana.api.JikanService
import com.aredruss.mangatana.data.datastore.SettingsDataStore
import com.aredruss.mangatana.utils.MediaMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class JikanRepository(
    private val jikanService: JikanService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dataStore: SettingsDataStore,
    private val mediaMapper: MediaMapper,
) {

    suspend fun searchForMedia(type: String, title: String) = flow {
        kotlinx.coroutines.delay(DEBOUNCE_TIME)

        val results = jikanService.searchByTitle(
            type = type,
            title = title,
            sfw = dataStore.allowNsfw().first()
        ).results

        Timber.e("results:")
        results.forEach {
            Timber.e(it.toString())
        }

        emit(
            mediaMapper.mapToMediaList(
                media = results ,
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
