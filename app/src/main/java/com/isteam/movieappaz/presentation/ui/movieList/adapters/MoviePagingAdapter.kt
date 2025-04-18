package com.isteam.movieappaz.presentation.ui.movieList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemMovieBinding
import com.isteam.movieappaz.domain.model.MovieUiModel

class MoviePagingAdapter :
    PagingDataAdapter<MovieUiModel, MoviePagingAdapter.MovieListViewHolder>(MovieItemCallback) {


    var onClickItem: (MovieUiModel) -> Unit = {}

    var onLongClickItem: (MovieUiModel, FragmentNavigator.Extras) -> Unit =
        { movieUiModel, extras -> }

    var getItemPosition: (Int) -> Unit = {}

    inner class MovieListViewHolder(private val itemMovieBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        fun bind(movieUiModel: MovieUiModel) {
            with(itemMovieBinding) {
                movieItem = movieUiModel

                cardRoot.setSafeOnClickListener {
                    onClickItem(movieUiModel)
                }
                executePendingBindings()
                cardRoot.transitionName = "card${movieUiModel.image}"
                imageView3.transitionName = movieUiModel.image
                val extras = FragmentNavigatorExtras(
                    cardRoot to cardRoot.transitionName,
                    imageView3 to imageView3.transitionName
                )
                cardRoot.setOnLongClickListener {
                    onLongClickItem(movieUiModel, extras)
                    true
                }
            }
        }

        fun setItemPosition(position: Int) {
            getItemPosition(position)
        }
    }

    object MovieItemCallback : DiffUtil.ItemCallback<MovieUiModel>() {
        override fun areItemsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.setItemPosition(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}