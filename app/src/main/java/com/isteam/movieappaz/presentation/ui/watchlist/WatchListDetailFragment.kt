package com.isteam.movieappaz.presentation.ui.watchlist

import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.databinding.FragmentWatchListDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListDetailFragment : BaseFragment<FragmentWatchListDetailBinding>(
    FragmentWatchListDetailBinding::inflate
) {
    override fun onViewCreateFinish() {
        setPlayer()
        lifecycle.addObserver(binding.youtubePlayerWatchList)
    }

    override fun observeEvents() {

    }

    private fun setPlayer() {
        binding.youtubePlayerWatchList.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo("S0Q4gqBUs7c", 0f);
            }
        })
    }

}