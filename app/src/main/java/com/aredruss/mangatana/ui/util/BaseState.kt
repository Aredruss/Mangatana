package com.aredruss.mangatana.ui.util

sealed class BaseState<out T : Any> {
    object Loading : BaseState<Nothing>()
    object Empty : BaseState<Nothing>()
    class Success<out T : Any>(val payload: T) : BaseState<T>()
    class Error(val error: Throwable) : BaseState<Nothing>()
}
