package com.aredruss.mangatana.view.media.list

import androidx.recyclerview.widget.RecyclerView
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.ListItemMediaBinding
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.util.GlideHelper

class MediaViewHolder(
    private val binding: ListItemMediaBinding,
    private val open: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val MAX_COVER_HEIGHT = 270
        private const val MAX_COVER_WIDTH = 170
        private const val MIN_COVER_HEIGHT = 150
        private const val MIN_COVER_WIDTH = 100

    }

    fun onBind(media: MediaDb, useMaxImages: Boolean) = with(binding) {

        root.setOnClickListener {
            open(media.malId)
        }

        titleTv.text = media.title

        GlideHelper.loadCover(
            context(),
            media.imageUrl,
            coverIv
        )
    }
}
