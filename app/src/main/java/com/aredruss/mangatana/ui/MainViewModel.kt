package com.aredruss.mangatana.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.data.model.MediaResponse
import com.aredruss.mangatana.domain.DatabaseInteractor
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.domain.MediaMapper
import com.aredruss.mangatana.ui.media.list.LiteMedia
import com.aredruss.mangatana.ui.util.BaseState
import com.aredruss.mangatana.ui.util.ScreenCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val jikanInteractor: JikanInteractor,
    private val databaseInteractor: DatabaseInteractor
) : ViewModel() {

    var mediaList = MutableLiveData<BaseState<ArrayList<LiteMedia>>>()
    var mediaInfo = MutableLiveData<BaseState<MediaResponse>>()
    var mediaType = MutableLiveData(JikanInteractor.TYPE_MANGA)

    fun getMediaDetails(malId: Long, type: String) {
        mediaInfo.postValue(BaseState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mediaInfo.postValue(
                    BaseState.Success(
                        jikanInteractor.getMedia(
                            type, malId
                        )
                    )
                )
            } catch (e: Throwable) {
                mediaInfo.postValue(BaseState.Error(e))
            }
        }
    }

    //TODO REMOVE MAPPERS FROM VIEWMODEL
    fun saveMedia(status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (mediaInfo.value is BaseState.Success) {
                    val currentMedia = (mediaInfo.value as BaseState.Success).payload
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

    fun getContent(mtype: String? = JikanInteractor.TYPE_MANGA, screenCategory: Int) {
        if (mtype != null && mtype != mediaType.value) {
            getMedia(mtype, screenCategory)
            mediaType.postValue(mtype)
        } else if (mediaList.value == null) {
            getMedia(mediaType.value ?: JikanInteractor.TYPE_MANGA, screenCategory)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseInteractor.clear()
        }
    }

    private fun getMedia(type: String, screenCategory: Int) {
        when (screenCategory) {
            ScreenCategory.IN_PROGRESS -> getSavedMedia(MediaDb.ONGOING_STATUS, type)
            ScreenCategory.BACKLOG -> getSavedMedia(MediaDb.BACKLOG_STATUS, type)
            ScreenCategory.FINISHED -> getSavedMedia(MediaDb.FINISHED_STATUS, type)
            ScreenCategory.STARRED -> {
            }
            else -> exploreMedia(type)
        }
    }

    private fun exploreMedia(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                mediaList.postValue(BaseState.Loading)
                mediaList.postValue(BaseState.Success(jikanInteractor.getTopMedia(type)))
            } catch (e: Throwable) {
                mediaList.postValue(BaseState.Error(e))
            }
        }
    }

    private fun getSavedMedia(status: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaList.postValue(BaseState.Loading)
            try {
                mediaList.postValue(
                    BaseState.Success(
                        MediaMapper.mapToLiteMediaList(
                            databaseInteractor.getSavedMedia(
                                status,
                                type
                            )
                        )
                    )
                )
            } catch (e: Throwable) {
                mediaList.postValue(BaseState.Error(e))
            }
        }
    }
}