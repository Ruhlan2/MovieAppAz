package com.isteam.movieappaz.presentation.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.enableTransitionAnimation
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.loadImageUrl
import com.isteam.movieappaz.common.utils.preview
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentHomeBinding
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.presentation.ui.home.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val viewModel by viewModels<HomeViewModel>()
    private val top10MovieAdapter = MovieAdapter()
    private val newMovieAdapter = MovieAdapter()
    private val comingSoonAdapter = MovieAdapter()
    private lateinit var movieUiModel: MovieUiModel

    override fun onResume() {
        super.onResume()
        changeTransitionState(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableTransitionAnimation()
    }

    private fun changeTransitionState(isReverse: Boolean) {
        with(binding) {
            if (isReverse) {
                if (constraintLayout7.currentState == R.id.end) {
                    constraintLayout7.transitionToStart()
                }
            } else {
                if (constraintLayout7.currentState == R.id.end) {
                    constraintLayout7.transitionToEnd()
                } else {
                    constraintLayout7.transitionToStart()
                }
            }
        }
    }

    override fun onViewCreateFinish() {
        postponeEnterTransition()
        setup()
    }


    override fun observeEvents() {
        val pd = requireActivity().createProgressDialog()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is HomeUiState.NowPlayingMovies -> newMovieAdapter.submitData(it.nowPlayingMovies)
                is HomeUiState.PopularMovies -> {

                    top10MovieAdapter.submitData(it.popularMovies)
                    binding.imageSliderHome.setImageList(it.imageList, ScaleTypes.CENTER_CROP)
                    binding.imageSliderHome.setSlideAnimation(AnimationTypes.ZOOM_OUT)
                    movieUiModel = it.movie
                    setUI()
                }

                is HomeUiState.Loading -> {
                    pd.show()
                }

                is HomeUiState.OnStopLoading -> {
                    pd.cancel()
                }

                is HomeUiState.UpComingMovies -> {
                    comingSoonAdapter.submitData(it.upComingMovies)
                    pd.cancel()
                }

                is HomeUiState.ProfileData -> {
                    pd.cancel()
                    binding.data = it.data
                    viewModel.getHomeData()
                }

                is HomeUiState.ChangedMovie -> {
                    movieUiModel = it.item
                    setUI()
                }

                else -> {}
            }
        }
    }

    private fun setup() {
        setRecycler()
        with(binding) {
            buttonSeeAll10.setSafeOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMovieListFragment(
                        MovieTypeEnum.POPULAR_MOVIES
                    )
                )
            }

            buttonNotificationHome.setSafeOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToNotificationsFragment()
                )
            }

            buttonSeeAllNew.setSafeOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMovieListFragment(
                        MovieTypeEnum.NOW_PLAYING
                    )
                )
            }
            buttonSeeAllComingSoon.setSafeOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMovieListFragment(
                        MovieTypeEnum.UPCOMING
                    )
                )
            }

            buttonChange.setSafeOnClickListener {
                viewModel.changeMovie()
            }

            buttonPlay.setSafeOnClickListener {
                if (this@HomeFragment::movieUiModel.isInitialized) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionGlobalMovieDetailsFragment(
                            movieUiModel.id
                        )
                    )
                }
            }

            buttonMoveToTop.setSafeOnClickListener {
                constraintLayout7.transitionToEnd()
            }

            constraintLayout7.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {

                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {

                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    motionLayout?.let {
                        if (it.currentState == R.id.end) {

                            constraintLayout3.setBackgroundColor(
                                resources.getColor(
                                    R.color.white,
                                    null
                                )
                            )
                            textView4.setTextColor(resources.getColor(R.color.red_21, null))
                            buttonNotificationHome.strokeColor =
                                ColorStateList.valueOf(resources.getColor(R.color.red_21, null))
                            buttonNotificationHome.iconTint =
                                ColorStateList.valueOf(resources.getColor(R.color.red_21, null))
                        } else {
                            constraintLayout3.setBackgroundColor(
                                resources.getColor(
                                    android.R.color.transparent,
                                    null
                                )
                            )
                            textView4.setTextColor(resources.getColor(R.color.white, null))
                            buttonNotificationHome.strokeColor =
                                ColorStateList.valueOf(resources.getColor(R.color.white, null))
                            buttonNotificationHome.iconTint =
                                ColorStateList.valueOf(resources.getColor(R.color.white, null))
                        }
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }

            })

        }
    }

    private fun setUI() {
        with(binding) {
            ivImage.loadImageUrl(Constants.BASE_URL_IMAGE + movieUiModel.backdropPath)
            if (movieUiModel.overview.isNotEmpty()) {
                tvDescription.text = movieUiModel.overview
                tvDescription.visible()
            } else {
                tvDescription.gone()
            }
            tvTitleMovie.text = movieUiModel.title
        }
    }

    private fun setRecycler() {
        with(binding) {
            rvTopMovieHome.preview(this@HomeFragment)
            rvNewReleaseHome.preview(this@HomeFragment)
            rvComingSoonHome.preview(this@HomeFragment)

            rvTopMovieHome.adapter = top10MovieAdapter
            rvNewReleaseHome.adapter = newMovieAdapter
            rvComingSoonHome.adapter = comingSoonAdapter

        }

        top10MovieAdapter.onClickItem = {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalMovieDetailsFragment(it.id)
            )
        }

        newMovieAdapter.onClickItem = {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalMovieDetailsFragment(it.id)
            )
        }

        comingSoonAdapter.onClickItem = {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalMovieDetailsFragment(it.id)
            )
        }

        top10MovieAdapter.onLongClickItem = { model, extra ->
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalImagePreviewFragment2(model.title, model.image),
                extra
            )
        }
        newMovieAdapter.onLongClickItem = { model, extra ->
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalImagePreviewFragment2(model.title, model.image),
                extra
            )
        }
        comingSoonAdapter.onLongClickItem = { model, extra ->
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalImagePreviewFragment2(model.title, model.image),
                extra
            )
        }
    }

    override fun onPause() {
        viewModel.onStopLoading()
        super.onPause()
        changeTransitionState(true)
    }


}