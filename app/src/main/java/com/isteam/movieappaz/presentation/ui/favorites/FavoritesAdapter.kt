package com.isteam.movieappaz.presentation.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemFavoritesBinding
import com.isteam.movieappaz.domain.model.favorites.FavoritesUiModel

class FavoritesAdapter : BaseAdapter<FavoritesUiModel>() {

    var onClickItem: (Int) -> Unit = {}

    var onClickDeleteButton: (Long) -> Unit = {}

    inner class FavoritesVH(private val binding: ItemFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: FavoritesUiModel) {
            with(binding) {
                data = item

                root.setSafeOnClickListener {
                    onClickItem(item.movieId.toInt())
                }

                buttonDelete.setSafeOnClickListener {
                    onClickDeleteButton(item.movieId)
                }

                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = FavoritesVH(
        ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoritesVH -> {
                items.getOrNull(position)?.let {
                    holder.find(it)
                }
            }
        }
    }

}