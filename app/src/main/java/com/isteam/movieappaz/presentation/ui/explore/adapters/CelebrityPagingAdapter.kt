package com.isteam.movieappaz.presentation.ui.explore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemCelebrityBinding
import com.isteam.movieappaz.domain.model.celebrities.CelebritiesUiModel

class
CelebrityPagingAdapter :
    PagingDataAdapter<CelebritiesUiModel, CelebrityPagingAdapter.CelebrityViewHolder>(
        CelebrityCallback
    ) {

    var onClick:(CelebritiesUiModel)->Unit={}

    inner class CelebrityViewHolder(private val itemCelebrityBinding: ItemCelebrityBinding) :RecyclerView.ViewHolder(itemCelebrityBinding.root){
        fun bind(celebritiesUiModel: CelebritiesUiModel){
            with(itemCelebrityBinding){
                celebrityItem=celebritiesUiModel
                root.setSafeOnClickListener {
                    onClick(celebritiesUiModel)
                }
                executePendingBindings()
            }
        }
    }


    object CelebrityCallback:DiffUtil.ItemCallback<CelebritiesUiModel>(){
        override fun areItemsTheSame(
            oldItem: CelebritiesUiModel,
            newItem: CelebritiesUiModel
        ): Boolean {
            return oldItem.hashCode()==newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: CelebritiesUiModel,
            newItem: CelebritiesUiModel
        ): Boolean {
            return oldItem==newItem
        }

    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {
        getItem(position)?.let{
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebrityViewHolder {
        return CelebrityViewHolder(
            ItemCelebrityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

