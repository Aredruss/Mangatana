package com.aredruss.mangatana.view.extensions

import android.app.Activity
import com.aredruss.mangatana.view.MainActivity

fun Activity.setBarTitle(stringId: Int) {
    (this as MainActivity).supportActionBar?.setTitle(stringId)
}
