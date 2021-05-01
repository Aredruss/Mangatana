package com.aredruss.mangatana.view.util

import com.microsoft.appcenter.crashes.Crashes
import retrofit2.HttpException

object ErrorHelper {
    private const val NOT_FOUND = 404
    private const val TOO_MANY_REQUESTS = 429

    fun processError(e: Throwable): Boolean {
        return if (e is HttpException) {
            when (e.code()) {
                NOT_FOUND, TOO_MANY_REQUESTS -> false
                else -> {
                    Crashes.trackError(e)
                    true
                }
            }
        } else false
    }
}
