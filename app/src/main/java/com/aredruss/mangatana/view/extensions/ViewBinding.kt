package com.aredruss.mangatana.view.util

import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

fun ViewBinding.context() = root.context

fun ViewBinding.getString(id: Int) = root.context.getString(id)

fun ViewBinding.getColor(id: Int) = root.context.getColor(id)

fun ViewBinding.getDrawable(id: Int) = ContextCompat.getDrawable(root.context, id)
