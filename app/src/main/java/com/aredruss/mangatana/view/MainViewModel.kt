package com.aredruss.mangatana.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.repo.DatabaseRepository
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.home.HomeState
import com.aredruss.mangatana.view.media.info.DetailsState
import com.aredruss.mangatana.view.media.list.ListState
import com.aredruss.mangatana.view.util.ScreenCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val jikanRepository: JikanRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    var mediaList = MutableLiveData<ListState>()
    var mediaInfo = MutableLiveData<DetailsState>()
    var contentCategory = -1
    var mediaType: String = JikanRepository.TYPE_MANGA

    // Get Content for the list Screen
    fun getMediaList(tabType: String?, screenCategory: Int) {
        Timber.e("TABTYPE " + tabType)
        if (contentCategory != screenCategory) {
            contentCategory = screenCategory
            getMedia(mediaType, screenCategory)
            return
        }
        if (tabType == null) {
            if (mediaList.value is ListState.Success) {
                if ((mediaList.value as ListState.Success).payload.isNullOrEmpty()) {
                    getMedia(mediaType, screenCategory)
                }
            }
        } else if (tabType != mediaType) {
            mediaType = tabType
            getMedia(mediaType, screenCategory)
        }
    }

    // Get Info about a certain media piece - a manga series or an anime
    fun getMediaDetails(type: String, malId: Long) {
        Timber.e("TYPE " + type)
        Timber.e("MEDIA TYPE " + mediaType)
        mediaInfo.postValue(DetailsState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mediaInfo.postValue(
                    DetailsState.Success(
                        jikanRepository.getMedia(
                            type, malId
                        )
                    )
                )
            } catch (e: Throwable) {
                mediaInfo.postValue(DetailsState.Error(e))
                e.printStackTrace()
            }
        }
    }

    // Save media with a certain status
    fun saveMedia(status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (mediaInfo.value is DetailsState.Success) {
                    val currentMedia = (mediaInfo.value as DetailsState.Success).payload
                    databaseRepository.addNewMediaEntry(currentMedia, status)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    // Debug function - clears all the database
    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.clear()
        }
    }

    // Get Media either from the database or the API
    private fun getMedia(type: String, screenCategory: Int) {
        when (screenCategory) {
            ScreenCategory.ON_GOING -> getSavedMedia(MediaDb.ONGOING_STATUS, type)
            ScreenCategory.BACKLOG -> getSavedMedia(MediaDb.BACKLOG_STATUS, type)
            ScreenCategory.FINISHED -> getSavedMedia(MediaDb.FINISHED_STATUS, type)
            // ScreenCategory.STARRED -> getStarredMedia(type)
            else -> exploreMedia(type)
        }
    }

    // Get Media from the Jikan API
    private fun exploreMedia(type: String) {
        viewModelScope.launch {
            mediaList.postValue(ListState.Loading)
            try {
                mediaList.postValue(
                    ListState.Success(
                        jikanRepository.getTopMedia(
                            type
                        )
                    )
                )
            } catch (e: Throwable) {
                mediaList.postValue(ListState.Error(e))
                e.printStackTrace()
            }
        }
    }

    // Get Media from the Database
    private fun getSavedMedia(status: Int, type: String) {
        viewModelScope.launch {
            mediaList.postValue(ListState.Loading)
            try {
                val result = databaseRepository.getSavedMedia(status, type)
                mediaList.postValue(
                    if (result.isEmpty()) {
                        ListState.Empty
                    } else {
                        ListState.Success(result)
                    }
                )
            } catch (e: Throwable) {
                mediaList.postValue(ListState.Error(e))
            }
        }
    }

//    Get Starred Media from the Database
//    private fun getStarredMedia(type: String) {
//        mediaList.postValue(ListState.Loading)
//    }
}
