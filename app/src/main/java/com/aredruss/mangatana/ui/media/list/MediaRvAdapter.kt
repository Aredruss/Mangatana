package com.aredruss.mangatana.ui.media.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.mangatana.databinding.ListItemMediaBinding

class MediaRvAdapter(val open: (Long) -> Unit) : RecyclerView.Adapter<MediaViewHolder>() {

    private var mediaList = ArrayList<LiteMedia>()

    fun setMedia(mediaList: ArrayList<LiteMedia>) {
        val diffCallback = ListDiffUtillCallback(mediaList, this.mediaList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.mediaList.clear()
        this.mediaList.addAll(mediaList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ListItemMediaBinding
            .inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        return MediaViewHolder(binding, open)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.onBind(mediaList[position])
    }

    override fun getItemCount() = mediaList.size
}