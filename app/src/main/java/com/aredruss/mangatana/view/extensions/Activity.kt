package com.aredruss.mangatana.view.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.view.forEach
import com.aredruss.mangatana.view.MainActivity

fun Activity.setBarTitle(stringId: Int) {
    (this as MainActivity).supportActionBar?.setTitle(stringId)
}

fun Activity.menuItemsVisible(isVisible: Boolean) {
    (this as MainActivity).menu?.forEach {
        it.isVisible = isVisible
    }
}

fun Activity.backButtonVisible(isVisible: Boolean) {
    (this as MainActivity).supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(isVisible)
        setDisplayShowHomeEnabled(isVisible)
    }
}

fun Activity.openLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    startActivity(
        Intent.createChooser(shareIntent, "Open with..")
    )
}

fun Activity.shareLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    startActivity(
        Intent.createChooser(shareIntent, "Share to..")
    )
}
