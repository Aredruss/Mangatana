package com.aredruss.mangatana.view.extensions

import android.view.View

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
