package com.aredruss.mangatana.view.media.list

import androidx.recyclerview.widget.RecyclerView
import com.aredruss.mangatana.databinding.ListItemMediaBinding
import com.aredruss.mangatana.view.util.GlideHelper

class MediaViewHolder(
    private val binding: ListItemMediaBinding,
    private val open: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val COVER_HEIGHT = 290
        private const val COVER_WIDTH = 190
    }

    fun onBind(media: LiteMedia) {

        binding.root.setOnClickListener {
            open(media.id)
        }

        GlideHelper.loadCover(
            binding.root.context,
            media.imageUrl,
            binding.mediaCoverIv,
            COVER_WIDTH,
            COVER_HEIGHT
        )
    }
}