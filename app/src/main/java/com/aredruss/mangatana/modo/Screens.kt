package com.aredruss.mangatana.modo

import com.aredruss.mangatana.view.home.HomeFragment
import com.aredruss.mangatana.view.media.info.MediaInfoFragment
import com.aredruss.mangatana.view.media.list.MediaListFragment
import com.github.terrakok.modo.AppScreen

@Suppress("FunctionNaming")
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
