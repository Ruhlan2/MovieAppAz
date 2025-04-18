package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.databinding.ItemDetailsGenreBinding
import com.isteam.movieappaz.domain.model.details.movieDetails.GenreDetailsUiModel


class DetailsGenreAdapter : BaseAdapter<GenreDetailsUiModel>() {

    inner class DetailsGenreVH(private val binding: ItemDetailsGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: GenreDetailsUiModel) {
            with(binding) {
                genre = item
                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = DetailsGenreVH(
        ItemDetailsGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DetailsGenreVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }
}

