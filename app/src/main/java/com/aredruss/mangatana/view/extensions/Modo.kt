package com.aredruss.mangatana.view.extensions

import androidx.core.view.forEach
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.view.MainActivity
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back

fun Modo.backWithAction(activity: MainActivity) {
    back()
    activity.menu?.forEach {
        it.isVisible = when (state.chain.last()) {
            is Screens.About, is Screens.Settings -> false
            else -> true
        }
    }
}
