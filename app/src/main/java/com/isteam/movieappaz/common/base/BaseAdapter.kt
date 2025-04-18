package com.isteam.movieappaz.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items = mutableListOf<T>()

    abstract fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        onCreate(
            parent = parent,
            inflater = LayoutInflater.from(parent.context),
            viewType = viewType
        )

    override fun getItemCount(): Int = items.size

    fun submitData(data: List<T>) {
        val diffCallback = DiffUtilCallback(items, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    class DiffUtilCallback<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            newList.getOrNull(newItemPosition).hashCode() == oldList.getOrNull(oldItemPosition)
                .hashCode()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
    }

}