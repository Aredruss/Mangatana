package com.aredruss.mangatana.ui.media.list

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

class ListDiffUtillCallback(
    private val newList: ArrayList<LiteMedia>,
    private val oldList: ArrayList<LiteMedia>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].id == newList[newPosition].id
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