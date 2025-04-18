package com.isteam.movieappaz.presentation.ui.explore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemGenreBinding
import com.isteam.movieappaz.domain.model.GenreUiModel

class GenreAdapter : BaseAdapter<GenreUiModel>() {

    var onClick: (GenreUiModel) -> Unit = {}

    inner class GenreViewHolder(private val itemGenreBinding: ItemGenreBinding) :
        RecyclerView.ViewHolder(itemGenreBinding.root) {
        fun bind(genreUiModel: GenreUiModel) {
            itemGenreBinding.genre = genreUiModel
            itemGenreBinding.executePendingBindings()

            itemGenreBinding.root.setSafeOnClickListener {
                onClick(genreUiModel)
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = GenreViewHolder(
        ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            when (holder) {
                is GenreViewHolder -> holder.bind(it)
            }
        }
    }
}