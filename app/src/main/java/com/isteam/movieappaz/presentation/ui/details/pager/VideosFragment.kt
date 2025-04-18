package com.isteam.movieappaz.presentation.ui.details.pager

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentVideosBinding
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedUiState
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieDetailsViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieUiState
import com.isteam.movieappaz.presentation.ui.details.adapters.VideosAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class VideosFragment : BaseFragment<FragmentVideosBinding>(FragmentVideosBinding::inflate) {

    private val svm by activityViewModels<MovieDetailsSharedViewModel>()
    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val adapter = VideosAdapter()

    override fun onViewCreateFinish() {

        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            with(binding) {
                state.observe(viewLifecycleOwner) {
                    when (it) {
                        is MovieUiState.Error -> {
                            progressBar.gone()
                            requireActivity().showMotionToast(
                                resources.getString(R.string.error),
                                it.message,
                                MotionToastStyle.ERROR
                            )
                        }

                        MovieUiState.Loading -> {
                            progressBar.visible()
                        }

                        is MovieUiState.Videos -> {
                            progressBar.gone()
                            adapter.submitData(it.data)
                            checkEmptyState()
                        }
                        else -> Unit
                    }
                }
            }
        }
        svm.state.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSharedUiState.MovieId -> {
                    viewModel.loadVideo(it.id)
                    requireActivity().changeLanguage(viewModel.getLanguageCode())
                }
            }
        }
    }

    private fun setup() {
        setRV()
    }

    private fun setRV() {
        with(binding) {
            rvVideos.adapter = adapter

            adapter.loadYoutubeView = { item, videoPlayer ->

                lifecycle.addObserver(videoPlayer)

                videoPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(item.key, 0f)
                        youTubePlayer.pause()
                    }
                })

            }
        }
    }

    private fun checkEmptyState() {
        with(binding) {
            if (adapter.itemCount <= 0 && rvVideos.isVisible) {
                rvVideos.gone()
                emptyLayout.visible()
            } else {
                emptyLayout.gone()
            }
        }
    }

}