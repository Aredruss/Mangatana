package com.aredruss.mangatana.view.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.aredruss.mangatana.R
import com.aredruss.mangatana.utils.ENABLE_LIGHT_THEME
import com.microsoft.appcenter.analytics.Analytics

fun Activity.openLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    startActivity(
        Intent.createChooser(shareIntent, getString(R.string.open_with))
    )
}

fun Activity.composeEmail() {
    val emailIntent = Intent().apply {
        action = Intent.ACTION_SENDTO
        data = Uri.parse("mailto:aredruss@gmail.com?subject=Mangatana")
    }
    startActivity(emailIntent)
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
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val netInfo = cm.getNetworkCapabilities(cm.activeNetwork)
        netInfo != null && netInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val netInfo = cm.activeNetworkInfo
        netInfo != null && netInfo.isConnected
    }
}

fun Activity.changeTheme(isDark: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (isDark) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            Analytics.trackEvent(ENABLE_LIGHT_THEME)
            AppCompatDelegate.MODE_NIGHT_NO
        }
    )
}
