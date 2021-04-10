package com.aredruss.mangatana.view.media.info

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsViewModel(
    private val jikanRepository: JikanRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel(), LifecycleObserver {

    var detailsState = MutableLiveData<DetailsState>(DetailsState.Loading)
    var mediaType: String = JikanRepository.TYPE_MANGA

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onFragmentDestroy() {
        detailsState.postValue(null)
        cancelJobs()
    }

    // Get Info about a certain media piece - a manga series or an anime
    fun getMediaDetails(type: String, malId: Long) = viewModelScope.launch {
        mediaType = type
        jikanRepository.getMedia(type, malId)
            .onStart {
                detailsState.postValue(DetailsState.Loading)
            }
            .zip(databaseRepository.getMediaEntry(malId, type)) { mediaResponse, mediaDb ->
                detailsState.postValue(DetailsState.Success(mediaResponse, mediaDb))
            }.catch { e ->
                detailsState.postValue(DetailsState.Error(e))
            }.collect()
    }

    // Save media with a certain status or update an existing entry
    fun editMediaEntry(status: Int, isStarred: Boolean) = viewModelScope.launch {
        if (detailsState.value is DetailsState.Success) {
            val successState = detailsState.value as DetailsState.Success
            databaseRepository.insertMediaEntry(
                successState.payload,
                mediaType,
                status,
                isStarred
            )
            databaseRepository.getMediaEntry(successState.payload.malId, mediaType)
                .catch { e -> detailsState.postValue(DetailsState.Error(e)) }
                .collect {
                    detailsState.postValue(DetailsState.Success(successState.payload, it))
                }
        }
    }

    fun deleteMediaEntry(mediaId: Long) = viewModelScope.launch {
        if (detailsState.value is DetailsState.Success) {
            val successPayload = (detailsState.value as DetailsState.Success).payload
            databaseRepository.deleteMediaEntry(mediaId, mediaType)
            detailsState.postValue(DetailsState.Success(successPayload, null))
        }
    }

    private fun cancelJobs() {
        jikanRepository.cancelAll()
        databaseRepository.cancelAll()
    }
}
