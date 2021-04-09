package com.aredruss.mangatana.view.extensions

import android.view.View
import androidx.appcompat.widget.SearchView

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun hideViews(views: List<View>) {
    views.forEach {
        it.gone()
    }
}

fun SearchView.clear() {
    setQuery("", false)
}

fun SearchView.hide() {
    this.gone()
    onActionViewCollapsed()
}
