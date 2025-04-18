package com.isteam.movieappaz.presentation.ui.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.ItemAiShowCaseBinding
import com.isteam.movieappaz.domain.model.AiShowCaseModel

class AiShowCaseAdapter : BaseAdapter<AiShowCaseModel>() {

    inner class AiShowCaseVH(private val binding: ItemAiShowCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AiShowCaseModel, position: Int) {
            with(binding) {
                textView22.text = item.message
                val animBT = AnimationUtils.loadAnimation(root.context, R.anim.bottom_to_top_anim)
                val animRL = AnimationUtils.loadAnimation(root.context, R.anim.right_to_left_anim)


                when (position) {
                    0 -> textView22.startAnimation(animBT)
                    1 -> textView22.startAnimation(animRL)
                    else -> textView22.clearAnimation()
                }



                if (item.lottieAnim == null) {
                    lottieAnimationView2.gone()
                } else {
                    lottieAnimationView2.visible()
                    lottieAnimationView2.setAnimation(item.lottieAnim)
                }
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = AiShowCaseVH(
        ItemAiShowCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AiShowCaseVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.bind(item, position)
                }
            }
        }
    }
}