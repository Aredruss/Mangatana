package com.aredruss.mangatana.view.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.aredruss.mangatana.R

fun Activity.openLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    startActivity(
        Intent.createChooser(shareIntent, getString(R.string.open_with))
    )
}

fun Activity.shareLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    startActivity(
        Intent.createChooser(shareIntent, getString(R.string.share_to))
    )
}

fun Activity.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.getNetworkCapabilities(cm.activeNetwork)
    return netInfo != null && netInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
