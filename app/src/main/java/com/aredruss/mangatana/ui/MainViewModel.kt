package com.aredruss.mangatana.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.domain.DatabaseInteractor
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.domain.MediaMapper
import com.aredruss.mangatana.ui.media.dialog.MediaInfoState
import com.aredruss.mangatana.ui.media.list.MediaListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val jikanInteractor: JikanInteractor,
    private val databaseInteractor: DatabaseInteractor
) : ViewModel() {

    var mediaList = MutableLiveData<MediaListState>()
    var mediaInfo = MutableLiveData<MediaInfoState>()

    fun exploreMedia(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (mediaList.value == null) {
                    mediaList.postValue(MediaListState.Loading)
                    mediaList.postValue(MediaListState.Success(jikanInteractor.getTopMedia(type)))
                }
            } catch (e: Throwable) {
                mediaList.postValue(MediaListState.Error(e))
            }
        }
    }

    fun getMediaDetails(malId: Long, type: String) {
        mediaInfo.postValue(MediaInfoState.InProgress)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mediaInfo.postValue(MediaInfoState.Success(jikanInteractor.getMedia(type, malId)))
            } catch (e: Throwable) {
                mediaInfo.postValue(MediaInfoState.Error(e))
            }
        }
    }

    fun saveMedia(status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (mediaInfo.value is MediaInfoState.Success) {
                    val currentMedia = (mediaInfo.value as MediaInfoState.Success).media
                    databaseInteractor.addNewMediaEntry(
                        MediaMapper.mapToMedia(
                            currentMedia,
                            status
                        )
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun getSavedMedia(status: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaList.postValue(MediaListState.Loading)
            try {
                mediaList.postValue(
                    MediaListState.Success(
                        MediaMapper.mapToLiteMediaList(
                            databaseInteractor.getSavedMedia(
                                status,
                                type
                            )
                        )
                    )
                )
            } catch (e: Throwable) {
                mediaList.postValue(MediaListState.Error(e))
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseInteractor.clear()
        }
    }
}