package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.databinding.ItemCommentsBinding
import com.isteam.movieappaz.domain.model.details.reviews.DetailsResultUiModel

class ReviewsPagingAdapter :
    PagingDataAdapter<DetailsResultUiModel, ReviewsPagingAdapter.ReviewsPagingVH>(ReviewsPagingDU) {


    inner class ReviewsPagingVH(private val binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: DetailsResultUiModel) {
            with(binding) {
                data = item
                executePendingBindings()
            }
        }
    }

    object ReviewsPagingDU : DiffUtil.ItemCallback<DetailsResultUiModel>() {
        override fun areItemsTheSame(
            oldItem: DetailsResultUiModel,
            newItem: DetailsResultUiModel
        ): Boolean = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: DetailsResultUiModel,
            newItem: DetailsResultUiModel
        ): Boolean = oldItem == newItem

    }

    override fun onBindViewHolder(holder: ReviewsPagingVH, position: Int) {
        getItem(position)?.let { item -> holder.find(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsPagingVH =
        ReviewsPagingVH(
            ItemCommentsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

}