package com.aredruss.mangatana.view.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {
    private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss'+'hh:mm"

    fun parseDate(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern(PATTERN, Locale.ENGLISH)
        return LocalDate.parse(date, inputFormatter).year.toString()
    }
}
