package com.aredruss.mangatana.view.util

import android.content.Context
import android.widget.ImageView
import com.aredruss.mangatana.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

object GlideHelper {

    private const val CORNER_RADIUS = 10

    fun loadCover(
        context: Context,
        imageUrl: String,
        imageView: ImageView
    ) {

        val imageLarge = imageUrl.replace(".jpg", "l.jpg")

        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(CORNER_RADIUS))

        Glide.with(context)
            .load(imageLarge)
            .fitCenter()
            .placeholder(
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    context.getColor(R.color.colorPrimary)
                } else {
                    context.resources.getColor(R.color.colorPrimary)
                }
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(imageView)
    }

}
