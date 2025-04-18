package com.isteam.movieappaz.presentation.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemMovieHomeBinding
import com.isteam.movieappaz.domain.model.MovieUiModel

class MovieAdapter : BaseAdapter<MovieUiModel>() {

    var onClickItem: (MovieUiModel) -> Unit = { }

    lateinit var onLongClickItem: (MovieUiModel, FragmentNavigator.Extras) -> Unit

    inner class MovieViewHolder(private val itemMovieBinding: ItemMovieHomeBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        fun bind(model: MovieUiModel, position: Int) {
            with(itemMovieBinding) {
                movieItem = model
                imageView3.transitionName = model.image
                cardRoot.transitionName = "card${model.image}"
                val extras = FragmentNavigatorExtras(
                    imageView3 to imageView3.transitionName,
                    cardRoot to cardRoot.transitionName
                )
                cardRoot.setSafeOnClickListener {
                    onClickItem(model)
                }

                cardRoot.setOnLongClickListener {
                    onLongClickItem(model, extras)
                    true
                }

                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = MovieViewHolder(
        ItemMovieHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            when (holder) {
                is MovieViewHolder -> holder.bind(it, position)
            }
        }
    }
}