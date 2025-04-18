package com.isteam.movieappaz.presentation.ui.movieAI.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.utils.AiRole
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.ItemMovieChatBinding
import com.isteam.movieappaz.domain.model.AiUiModel

class MovieChatAdapter : BaseAdapter<AiUiModel>() {

    var onClickItem: (AiUiModel) -> Unit = {}

    private inner class MovieChatVH(private val binding: ItemMovieChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: AiUiModel) {
            with(binding) {
                data = item

                if (item.aiRole == AiRole.USER) {
                    cardMyChat.visible()
                    cardPP.gone()
                    cardAiChat.gone()
                } else {
                    cardAiChat.visible()
                    cardPP.visible()
                    cardMyChat.gone()
                }

                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = MovieChatVH(
        ItemMovieChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieChatVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }


}