package com.aredruss.mangatana.view.extensions

import android.graphics.ColorFilter
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback

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

fun showViews(views: List<View>) {
    views.forEach {
        it.visible()
    }
}

fun Button.setDrawableEnd(@DrawableRes id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(context, id),
        null,
        null,
        null
    )
}

fun RadioButton.setIconText(@DrawableRes icon: Int?, @StringRes text: Int) {
    if (icon != null) setDrawableEnd(icon)
    setText(text)
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun RadioButton.setIconText(@DrawableRes icon: Int?, text: String) {
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

fun LottieAnimationView.changeLayersColor(
    @ColorRes colorRes: Int
) {
    val color = ContextCompat.getColor(context, colorRes)
    val filter = SimpleColorFilter(color)
    val keyPath = KeyPath("**")
    val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)

    addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
}
