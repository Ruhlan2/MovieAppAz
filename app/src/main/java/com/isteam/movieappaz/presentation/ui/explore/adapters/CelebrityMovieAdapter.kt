package com.isteam.movieappaz.presentation.ui.explore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemCelebrityMovieBinding
import com.isteam.movieappaz.domain.model.MovieUiModel

class CelebrityMovieAdapter:BaseAdapter<MovieUiModel>() {

    lateinit var onClick: (MovieUiModel) -> Unit

    inner class CelebrityMovieHolder(private val itemMovieBinding: ItemCelebrityMovieBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        fun bind(movieUiModel: MovieUiModel){
            with(itemMovieBinding){
                movieItem=movieUiModel

                cardRoot.setSafeOnClickListener {
                    onClick(movieUiModel)
                }
                executePendingBindings()
            }
        }
    }
    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder =CelebrityMovieHolder(
        ItemCelebrityMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items.getOrNull(position)?.let{
            when(holder){
                is CelebrityMovieHolder -> holder.bind(it)
            }
        }

    }
}