package com.aredruss.mangatana.view.extensions

import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

fun ViewBinding.context() = root.context

fun ViewBinding.getString(id: Int) = root.context.getString(id)

fun ViewBinding.getColor(id: Int) =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        root.context.getColor(id)
    } else {
        root.context.resources.getColor(id)
    }

fun ViewBinding.getDrawable(id: Int) = ContextCompat.getDrawable(root.context, id)
