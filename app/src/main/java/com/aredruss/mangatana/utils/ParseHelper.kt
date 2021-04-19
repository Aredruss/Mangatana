package com.aredruss.mangatana.utils

import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object ParseHelper {
    private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss'+'hh:mm"

    fun parseDate(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern(PATTERN, Locale.ENGLISH)
        return try {
            LocalDate.parse(date, inputFormatter).year.toString()
        } catch (e: DateTimeParseException) {
            Timber.e("Failded to parse date $date")
            ""
        }
    }

    fun parseAuthor(author: String): String {
        return author
            .replace(",", "")
            .split(" ")
            .reversed()
            .joinToString(" ")
    }
}
