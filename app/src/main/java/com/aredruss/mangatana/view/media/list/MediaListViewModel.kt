package com.aredruss.mangatana.view.media.list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.modo.ScreenCategory
import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.repo.JikanRepository.Companion.TYPE_MANGA
import com.aredruss.mangatana.view.extensions.Event
import com.aredruss.mangatana.view.extensions.update
import com.aredruss.mangatana.view.home.MediaListState
import com.aredruss.mangatana.view.util.ErrorHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MediaListViewModel(
    private val jikanRepository: JikanRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel(), LifecycleObserver {
    var mediaType: String = TYPE_MANGA

    val state = MutableLiveData(
        MediaListState(
            isLoading = true,
            isSearch = false,
            isEmpty = false,
            error = null,
            mediaType = TYPE_MANGA,
            content = null,
            screenCategory = -1
        )
    )


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onFragmentDestroy() {
        mediaType = TYPE_MANGA
        cancelJobs()
    }

    fun syncLists() {
        state.value?.let {
            if (it.screenCategory != ScreenCategory.EXPLORE) {
                getMedia(
                    it.mediaType,
                    it.screenCategory
                )
            }
        }
    }

    // Get Content for the list Screen
    // Need to check what type of content list should be populated with
    fun getMediaList(tabType: String?, screenCategory: Int) {
        if (tabType == state.value?.mediaType
            && screenCategory == state.value?.screenCategory
            && !state.value?.content.isNullOrEmpty()
        ) return
        state.update {
            it.copy(
                isLoading = true,
                isSearch = false,
                screenCategory = screenCategory,
                mediaType = tabType ?: TYPE_MANGA
            )
        }
        getMedia(tabType ?: TYPE_MANGA, screenCategory)
    }

    fun searchForMedia(query: String, type: String) {
        mediaType = type

        state.update { it.copy(isLoading = true, isSearch = true, mediaType = type) }

        viewModelScope.launch {
            when (state.value?.screenCategory) {
                ScreenCategory.ON_GOING,
                ScreenCategory.BACKLOG,
                ScreenCategory.FINISHED ->
                    search(
                        isLocal = true,
                        isFavorite = false,
                        query = query
                    )
                ScreenCategory.STARRED -> search(
                    isLocal = true,
                    isFavorite = true,
                    query = query
                )
                else -> search(
                    isLocal = false,
                    isFavorite = false,
                    query = query
                )
            }
        }
    }

    // Get Media either from the database or the API depending on the category of the screen
    private fun getMedia(type: String, screenCategory: Int) {
        when (screenCategory) {
            ScreenCategory.ON_GOING,
            ScreenCategory.BACKLOG,
            ScreenCategory.FINISHED ->
                getSavedMedia(screenCategory, type)
            ScreenCategory.STARRED -> getFavoriteMedia(type)
            else -> getTopMedia(type)
        }
    }

    // Get Media from the Jikan API
    private fun getTopMedia(type: String) = viewModelScope.launch {
        jikanRepository.getTopMediaList(type)
            .catch { e ->
                state.update { it.copy(isLoading = false, error = Event(e)) }
            }.collect { topMedia ->
                state.update { it.copy(isLoading = false, content = topMedia) }
            }
    }

    // Get Media from the Database
    private fun getSavedMedia(status: Int, type: String) = viewModelScope.launch {
        databaseRepository.getSavedMediaList(status, type)
            .catch { e ->
                postEmptyOrError(e)
            }
            .collect { list ->
                postEmptyOrSuccess(list)
            }
    }

    private fun getFavoriteMedia(type: String) = viewModelScope.launch {
        databaseRepository.getFavoriteMediaList(type)
            .catch { e ->
                postEmptyOrError(e)
            }
            .collect { list ->
                postEmptyOrSuccess(list)
            }
    }

    private suspend fun search(isLocal: Boolean, isFavorite: Boolean, query: String) {
        when {
            isLocal && !isFavorite -> databaseRepository.searchCategoryByName(
                mediaType,
                state.value?.screenCategory ?: 0,
                query
            )
            isFavorite -> databaseRepository.searchFavoriteByName(mediaType, query)
            else -> jikanRepository.searchForMedia(mediaType, query)
        }
            .catch { e ->
                postEmptyOrError(e)
            }
            .collect { list ->
                postEmptyOrSuccess(list)
            }
    }

    private fun postEmptyOrError(e: Throwable) {
        state.update {
            if (ErrorHelper.processError(e)) {
                it.copy(isLoading = false, error = Event(e), isEmpty = false)
            } else {
                it.copy(isLoading = false, isEmpty = true)
            }
        }
    }

    private fun postEmptyOrSuccess(list: List<MediaDb>) {
        state.update {
            if (list.isEmpty()) {
                it.copy(isLoading = false, isEmpty = true)
            } else {
                it.copy(isLoading = false, content = list, isEmpty = false)
            }
        }
    }

    private fun cancelJobs() {
        jikanRepository.cancelAll()
        databaseRepository.cancelAll()
    }
}
