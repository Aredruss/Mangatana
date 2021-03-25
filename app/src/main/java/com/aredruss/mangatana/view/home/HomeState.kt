package com.aredruss.mangatana.view.home

sealed class HomeState {
    object Loading : HomeState()
    object Empty : HomeState()
    class Success(val payload: Any) : HomeState()
    class Error(val error: Throwable) : HomeState()
}
