package com.aredruss.mangatana.data.datastore

data class AppState(
    val isDark: Boolean,
    val locale: String,
    val allowNsfw: Boolean
)