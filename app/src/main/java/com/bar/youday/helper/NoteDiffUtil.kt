package com.bar.youday.helper

import androidx.recyclerview.widget.DiffUtil
import com.bar.youday.data.Note

class NoteDiffUtil(
    private val oldList: List<Note>,
    private val newList: List<Note>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].text != newList[newItemPosition].text -> false
            oldList[oldItemPosition].title != newList[newItemPosition].title -> false
            oldList[oldItemPosition].type != newList[newItemPosition].type -> false
            else -> true
        }
    }
}