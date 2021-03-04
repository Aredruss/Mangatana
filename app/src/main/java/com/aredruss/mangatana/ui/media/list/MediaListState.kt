package com.aredruss.mangatana.ui.media.list

sealed class MediaListState {
    object Loading : MediaListState()
    class Success(val media: ArrayList<LiteMedia>) : MediaListState()
    class Error(val error: Throwable) : MediaListState()
}
