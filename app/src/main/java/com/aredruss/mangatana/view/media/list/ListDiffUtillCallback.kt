package com.aredruss.mangatana.view.media.list

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.aredruss.mangatana.data.database.MediaDb

class ListDiffUtillCallback(
    private val newList: ArrayList<MediaDb>,
    private val oldList: ArrayList<MediaDb>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].malId == newList[newPosition].malId
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (id, name, _) = oldList[oldPosition]
        val (id1, name1, _) = newList[newPosition]

        return name == name1 && id == id1
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}
