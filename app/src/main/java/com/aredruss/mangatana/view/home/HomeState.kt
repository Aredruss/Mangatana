package com.aredruss.mangatana.view.home

import com.aredruss.mangatana.data.database.MediaDb

sealed class HomeState {
    object Empty : HomeState()
    class Success(val payload: ArrayList<MediaDb>) : HomeState()
}
