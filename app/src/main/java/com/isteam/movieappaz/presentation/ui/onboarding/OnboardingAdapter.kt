package com.isteam.movieappaz.presentation.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.databinding.ItemOnboardingBinding
import com.isteam.movieappaz.domain.model.OnboardingUiModel

class OnboardingAdapter : BaseAdapter<OnboardingUiModel>() {

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingUiModel, position: Int) {
            with(binding) {
                data = item
                if (position == 0) {
                    val params = (imageView.layoutParams as ViewGroup.MarginLayoutParams)
                    params.setMargins(0, 0, 0, 0)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        OnboardingViewHolder(ItemOnboardingBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OnboardingViewHolder -> {
                items.getOrNull(position)?.let { item ->
                    holder.bind(item, position)
                }
            }
        }
    }

}