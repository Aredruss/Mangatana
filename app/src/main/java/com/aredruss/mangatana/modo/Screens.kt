package com.aredruss.mangatana.modo

import com.aredruss.mangatana.view.about.AboutFragment
import com.aredruss.mangatana.view.home.HomeFragment
import com.aredruss.mangatana.view.media.info.MediaInfoFragment
import com.aredruss.mangatana.view.media.list.MediaListFragment
import com.aredruss.mangatana.view.settings.SettingsFragment
import com.github.terrakok.modo.android.AppScreen
import kotlinx.parcelize.Parcelize

@Suppress("FunctionNaming")
object Screens {

    const val HOME = "home"
    const val LIST = "list"
    const val INFO = "info"
    const val ABOUT = "about"
    const val SETTINGS = "settings"

    @Parcelize
    class Home : AppScreen(HOME) {
        override fun create() = HomeFragment.create()
    }

    @Parcelize
    class MediaList(val category: Int) : AppScreen(LIST) {
        override fun create() = MediaListFragment.create(category)
    }

    @Parcelize
    class MediaInfo(val malId: Long, val type: String) : AppScreen(INFO) {
        override fun create() = MediaInfoFragment.create(malId, type)
    }

    @Parcelize
    class About : AppScreen(ABOUT) {
        override fun create() = AboutFragment.create()
    }

    @Parcelize
    class Settings : AppScreen(SETTINGS) {
        override fun create() = SettingsFragment.create()
    }
}
