package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemBackdropBinding
import com.isteam.movieappaz.domain.model.details.images.BackdropUiModel

class BackdropsAdapter : BaseAdapter<BackdropUiModel>() {

    lateinit var onClickItem: (BackdropUiModel, FragmentNavigator.Extras) -> Unit

    inner class BackdropVH(private val binding: ItemBackdropBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: BackdropUiModel) {
            with(binding) {
                data = item
                imageView3.transitionName = item.filePath
                cardRoot.transitionName = "card${item.filePath}"
                val extras = FragmentNavigatorExtras(
                    imageView3 to imageView3.transitionName,
                    cardRoot to cardRoot.transitionName
                )
                cardRoot.setSafeOnClickListener {
                    onClickItem(item, extras)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = BackdropVH(
        ItemBackdropBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BackdropVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }


}