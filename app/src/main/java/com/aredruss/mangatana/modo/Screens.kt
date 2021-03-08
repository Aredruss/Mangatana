package com.aredruss.mangatana.modo

import com.aredruss.mangatana.ui.home.HomeFragment
import com.aredruss.mangatana.ui.media.info.MediaInfoFragment
import com.aredruss.mangatana.ui.media.list.MediaListFragment
import com.github.terrakok.modo.AppScreen

object Screens {
    fun Home() = AppScreen("home") {
        HomeFragment.create()
    }

    fun MediaList(category: Int) = AppScreen("list") {
        MediaListFragment.create(category)
    }

    fun MediaInfo(malId: Long, type: String) = AppScreen("info") {
        MediaInfoFragment.create(malId, type)
    }
}