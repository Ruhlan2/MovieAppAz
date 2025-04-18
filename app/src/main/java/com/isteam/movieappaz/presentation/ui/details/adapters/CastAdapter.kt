package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.loadImageUrl
import com.isteam.movieappaz.databinding.ItemCreditsBinding
import com.isteam.movieappaz.domain.model.credits.CastUiModel

class CastAdapter : BaseAdapter<CastUiModel>() {

    var onClickItem: (CastUiModel) -> Unit = {}

    inner class CastVH(private val binding: ItemCreditsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: CastUiModel) {
            with(binding) {
                imagePP.loadImageUrl(Constants.BASE_URL_IMAGE + item.profilePath)
                textName.text = item.name
                textRole.text = item.character

                root.setSafeOnClickListener {
                    onClickItem(item)
                }
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder =
        CastVH(ItemCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CastVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }

}
