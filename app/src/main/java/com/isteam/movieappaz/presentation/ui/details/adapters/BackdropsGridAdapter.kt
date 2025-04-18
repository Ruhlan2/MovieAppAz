package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.ItemGridBackdropBinding
import com.isteam.movieappaz.domain.model.details.images.BackdropUiModel

class BackdropsGridAdapter : BaseAdapter<BackdropUiModel>() {

    lateinit var onLongClickItem: (BackdropUiModel, FragmentNavigator.Extras) -> Unit

    inner class BackdropVH(private val binding: ItemGridBackdropBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: BackdropUiModel) {
            with(binding) {
                data = item
                cardRoot.transitionName = "card${item.filePath}"
                backdropImage.transitionName = item.filePath

                val extras = FragmentNavigatorExtras(
                    cardRoot to cardRoot.transitionName,
                    backdropImage to backdropImage.transitionName
                )
                cardRoot.setSafeOnClickListener {
                    onLongClickItem(item, extras)
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
        ItemGridBackdropBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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