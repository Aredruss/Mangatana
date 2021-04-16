package com.aredruss.mangatana.view.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri

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
