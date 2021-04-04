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
        private const val COVER_HEIGHT = 270
        private const val COVER_WIDTH = 170
    }

    fun onBind(media: MediaDb) = with(binding) {

        root.setOnClickListener {
            open(media.malId)
        }

        titleTv.text = media.title

        GlideHelper.loadCover(
            binding.context(),
            media.imageUrl,
            binding.coverIv,
            COVER_WIDTH,
            COVER_HEIGHT
        )
    }
}
