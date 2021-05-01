package com.aredruss.mangatana.view.media.list

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.App
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.modo.ScreenCategory
import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.repo.JikanRepository.Companion.TYPE_MANGA
import com.aredruss.mangatana.view.util.ErrorCodes
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MediaListViewModel(
    private val jikanRepository: JikanRepository,
    private val databaseRepository: DatabaseRepository
) : AndroidViewModel(App.INSTANCE), LifecycleObserver {

    var listState = MutableLiveData<ListState>()
    var screenCategory = -1
    var mediaType: String = JikanRepository.TYPE_MANGA
    var isSearch: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onFragmentDestroy() {
        listState.postValue(null)
        cancelJobs()
    }

    // Get Content for the list Screen
    // Need to check what type of content list should be populated with
    fun getMediaList(tabType: String?, screenCategory: Int) {
        mediaType = tabType ?: TYPE_MANGA
        this.screenCategory = screenCategory
        getMedia(mediaType, screenCategory)
    }

    fun searchForMedia(query: String, type: String) {
        mediaType = type
        viewModelScope.launch {
            when (screenCategory) {
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
        jikanRepository.getTopMediaList(type).onStart {
            listState.postValue(ListState.Loading)
        }.catch { e ->
            listState.postValue(ListState.Error(e))
        }.collect { topMedia ->
            listState.postValue(ListState.Success(topMedia))
        }
    }

    // Get Media from the Database
    private fun getSavedMedia(status: Int, type: String) = viewModelScope.launch {
        databaseRepository.getSavedMediaList(status, type)
            .onStart {
                listState.postValue(ListState.Loading)
            }
            .catch { e ->
                listState.postValue(ListState.Error(e))
            }
            .collect { list ->
                listState.postValue(
                    if (list.isEmpty()) {
                        ListState.Empty
                    } else {
                        ListState.Success(list as ArrayList<MediaDb>)
                    }
                )
            }
    }

    private fun getFavoriteMedia(type: String) = viewModelScope.launch {
        databaseRepository.getFavoriteMediaList(type)
            .onStart {
                listState.postValue(ListState.Loading)
            }
            .catch { e ->
                listState.postValue(ListState.Error(e))
            }
            .collect { list ->
                listState.postValue(
                    if (list.isEmpty()) {
                        ListState.Empty
                    } else {
                        ListState.Success(list as ArrayList<MediaDb>)
                    }
                )
            }
    }

    private suspend fun search(isLocal: Boolean, isFavorite: Boolean, query: String) {
        when {
            isLocal && !isFavorite -> databaseRepository.searchCategoryByName(
                mediaType,
                screenCategory,
                query
            )
            isFavorite -> databaseRepository.searchFavoriteByName(mediaType, query)
            else -> jikanRepository.searchForMedia(mediaType, query)
        }
            .onStart {
                listState.postValue(ListState.Loading)
            }
            .catch { e ->
                if (e is HttpException) {
                    listState.postValue(
                        when (e.code()) {
                            ErrorCodes.NOT_FOUND -> ListState.Empty
                            else -> ListState.Error(e)
                        }
                    )
                }
            }
            .collect { list ->
                listState.postValue(
                    if (list.isEmpty()) {
                        ListState.Empty
                    } else {
                        ListState.Success(list as ArrayList<MediaDb>)
                    }
                )
            }
    }

    private fun cancelJobs() {
        jikanRepository.cancelAll()
        databaseRepository.cancelAll()
    }
}
