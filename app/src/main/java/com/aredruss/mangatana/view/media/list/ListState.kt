package com.aredruss.mangatana.view.media.list

sealed class ListState {
    object Loading : ListState()
    object Empty : ListState()
    class Success(val payload: ArrayList<LiteMedia>) : ListState()
    class Error(val error: Throwable) : ListState()
}
