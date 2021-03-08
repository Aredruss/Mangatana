package com.aredruss.mangatana.ui.media.list

import androidx.recyclerview.widget.RecyclerView
import com.aredruss.mangatana.databinding.ListItemMediaBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

class MediaViewHolder(
    private val binding: ListItemMediaBinding,
    private val open: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(media: LiteMedia) {

        binding.root.setOnClickListener {
            Timber.e(media.id.toString() + " IDDDD")
            open(media.id)
        }

        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(10))

        Glide.with(binding.root.context)
            .load(media.imageUrl)
            .apply(RequestOptions().override(190, 290))
            .apply(requestOptions)
            .into(binding.mediaCoverIv)
    }
}