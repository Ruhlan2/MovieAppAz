package com.isteam.movieappaz.presentation.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.databinding.ItemVideoBinding
import com.isteam.movieappaz.domain.model.details.video.VideoResultUiModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideosAdapter : BaseAdapter<VideoResultUiModel>() {

    var loadYoutubeView: (VideoResultUiModel, YouTubePlayerView) -> Unit = { _, _ -> }

    inner class VideosVH(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: VideoResultUiModel) {
            with(binding) {
                data = item
                loadYoutubeView(item, videoPlayer)

                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = VideosVH(
        ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideosVH -> {
                items.getOrNull(position)?.let { item ->
                    holder.find(item)
                }
            }
        }
    }
}