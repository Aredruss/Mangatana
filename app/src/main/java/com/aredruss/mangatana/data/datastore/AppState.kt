package com.aredruss.mangatana.data.datastore

data class AppState(
    val darkModeState: Int,
    val allowNsfw: Boolean
) {
    companion object {
        const val DARK_MODE_AUTO = 0
        const val DARK_MODE_DISABLED = 1
        const val DARK_MODE_ENABLED = 2
    }
}

