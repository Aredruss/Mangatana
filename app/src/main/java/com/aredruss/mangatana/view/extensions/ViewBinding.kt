package com.aredruss.mangatana.view.extensions

import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

fun ViewBinding.context() = root.context

fun ViewBinding.getString(id: Int) = root.context.getString(id)

fun ViewBinding.getInterpolatedString(
    id: Int,
    vararg args: String
): String {
    return root.context.getString(id, args)
}

fun ViewBinding.getColor(id: Int) = root.context.getColor(id)

fun ViewBinding.getDrawable(id: Int) = ContextCompat.getDrawable(root.context, id)
