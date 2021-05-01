package com.aredruss.mangatana.view.media.list

import com.aredruss.mangatana.data.database.MediaDb

sealed class ListState {
    object Loading : ListState()
    object Empty : ListState()
    class Success(val payload: ArrayList<MediaDb>) : ListState()
    class Error(val error: Throwable) : ListState()
}
