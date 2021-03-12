package com.aredruss.mangatana.view.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object GlideHelper {

    private const val CORNER_RADIUS = 10

    fun loadCover(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        width: Int,
        height: Int
    ) {
        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(CORNER_RADIUS))

        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions().override(width, height))
            .apply(requestOptions)
            .into(imageView)
    }
}
