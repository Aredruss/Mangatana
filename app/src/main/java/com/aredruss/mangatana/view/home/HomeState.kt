package com.aredruss.mangatana.view.home

import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.view.extensions.Event

sealed class HomeState {
    object Empty : HomeState()
    class Success(val payload: ArrayList<MediaDb>) : HomeState()
}

data class MediaListState(
    var isLoading: Boolean,
    var isSearch: Boolean,
    var isEmpty: Boolean,
    var error: Event<Throwable>?,
    var mediaType: String,
    var screenCategory: Int,
    var content: List<MediaDb>?
)
