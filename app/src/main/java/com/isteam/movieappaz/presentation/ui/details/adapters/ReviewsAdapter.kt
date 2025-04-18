package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.databinding.ItemCommentsBinding
import com.isteam.movieappaz.domain.model.details.reviews.DetailsResultUiModel

class ReviewsAdapter : BaseAdapter<DetailsResultUiModel>() {

    inner class ReviewsVH(private val binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: DetailsResultUiModel) {
            with(binding) {
                data = item
                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = ReviewsVH(
        ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReviewsVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }


}