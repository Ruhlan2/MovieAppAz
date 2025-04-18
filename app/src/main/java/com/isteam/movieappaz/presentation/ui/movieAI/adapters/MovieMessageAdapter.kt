package com.isteam.movieappaz.presentation.ui.movieAI.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemAiMessageBinding
import com.isteam.movieappaz.domain.model.MessageUiModel

class MovieMessageAdapter : BaseAdapter<MessageUiModel>() {

    var onClickItem: (MessageUiModel) -> Unit = {}

    private inner class MovieMessageVH(
        private val binding: ItemAiMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun find(item: MessageUiModel) {
            with(binding) {
                data = item

                cardMessage.setSafeOnClickListener {
                    onClickItem(item)
                }

                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = MovieMessageVH(
        ItemAiMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieMessageVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }

}