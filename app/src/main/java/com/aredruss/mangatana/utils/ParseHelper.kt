package com.aredruss.mangatana.utils

import com.aredruss.mangatana.model.Genre
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object ParseHelper {
    private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss'+'hh:mm"

    fun parseDate(date: String): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern(PATTERN, Locale.ENGLISH)
            try {
                LocalDate.parse(date, inputFormatter).year.toString()
            } catch (e: DateTimeParseException) {
                Timber.e("Failded to parse date $date")
                ""
            }
        } else {
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

    fun parseGenres(genres: List<Genre>): String {
        return genres.map {
            it.name
        }
            .toString()
            .removePrefix("[")
            .removeSuffix("]")
    }
}
