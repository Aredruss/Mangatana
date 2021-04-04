package com.aredruss.mangatana.view.util

import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {
    private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss'+'hh:mm"

    fun parseDate(date: String): String {
        Timber.e(date)
        val inputFormatter = DateTimeFormatter.ofPattern(PATTERN, Locale.ENGLISH)
        Timber.e("DATE  " + LocalDate.parse(date, inputFormatter).year.toString())
        return LocalDate.parse(date, inputFormatter).year.toString()
    }
}
