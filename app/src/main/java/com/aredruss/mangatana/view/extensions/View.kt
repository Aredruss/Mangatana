package com.aredruss.mangatana.view.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat

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

fun Button.setDrawableEnd(id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(context, id),
        null,
        null,
        null
    )
}

fun RadioButton.setIconText(icon: Int?, text: Int) {
    if (icon != null) setDrawableEnd(icon)
    setText(text)
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun RadioButton.setIconText(icon: Int?, text: String) {
    if (icon != null) setDrawableEnd(icon)
    setText(text)
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun SearchView.clear() {
    setQuery("", false)
}

fun SearchView.hide() {
    this.gone()
    onActionViewCollapsed()
}
