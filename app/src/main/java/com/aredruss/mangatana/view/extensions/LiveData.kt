package com.aredruss.mangatana.view.extensions

import androidx.lifecycle.MutableLiveData

inline fun <T> MutableLiveData<T>.update(crossinline block: (T) -> T) {
    value?.let { value = block(it) }
}

class Event<T>(private val content: T) {
    private var consumed = false

    fun peek(): T = content

    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }
}