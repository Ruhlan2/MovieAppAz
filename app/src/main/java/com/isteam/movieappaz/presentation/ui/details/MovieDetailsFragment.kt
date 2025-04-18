package com.isteam.movieappaz.presentation.ui.details

import android.content.res.Resources
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.enableTransitionAnimation
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.preview
import com.isteam.movieappaz.common.utils.showDialog
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesBody
import com.isteam.movieappaz.databinding.FragmentMovieDetailsBinding
import com.isteam.movieappaz.domain.model.details.movieDetails.MovieDetailsUiModel
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedUiState
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedViewModel
import com.isteam.movieappaz.presentation.ui.details.adapters.BackdropsAdapter
import com.isteam.movieappaz.presentation.ui.details.adapters.CastAdapter
import com.isteam.movieappaz.presentation.ui.details.adapters.CrewAdapter
import com.isteam.movieappaz.presentation.ui.details.adapters.DetailsGenreAdapter
import com.isteam.movieappaz.presentation.ui.details.adapters.PagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {

    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val svm by activityViewModels<MovieDetailsSharedViewModel>()
    private val args by navArgs<MovieDetailsFragmentArgs>()
    private lateinit var pagerAdapter: PagerAdapter
    private val castAdapter = CastAdapter()
    private val crewAdapter = CrewAdapter()
    private val genreAdapter = DetailsGenreAdapter()
    private val backdropAdapter = BackdropsAdapter()
    private lateinit var uiModel: MovieDetailsUiModel
    private lateinit var favoritesBody: FavoritesBody
    private val pd by lazy {
        requireActivity().createProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        svm.setId(args.id)
        enableTransitionAnimation()
    }

    override fun onViewCreateFinish() {
        setVP()
        postponeEnterTransition()
        viewModel.initialCall(args.id)
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is MovieUiState.Error -> {
                        pd.cancel()
                        requireContext().showDialog(
                            resources.getString(R.string.error),
                            it.message,
                            action = {
                                findNavController().popBackStack()
                            }
                        )
                    }

                    MovieUiState.Loading -> pd.show()

                    is MovieUiState.MovieDetails -> {
                        with(it.data) {
                            binding.data = this
                            uiModel = it.data
                            favoritesBody = FavoritesBody(
                                id.toLong(), title, overview, backdropPath
                            )
                            genreAdapter.submitData(it.data.genres)
                        }
                        setUI()
                    }

                    is MovieUiState.Credits -> {
                        castAdapter.submitData(it.data.cast)
                        crewAdapter.submitData(it.data.crew)
                    }

                    is MovieUiState.Images -> {
                        pd.cancel()
                        if (it.data.isEmpty()) {
                            binding.layoutImage.gone()
                        } else {
                            backdropAdapter.submitData(it.data)
                        }
                    }

                    is MovieUiState.IsInFavorite -> {
                        binding.isInFavorite = it.data
                        if (!it.initialCall) {
                            pd.cancel()
                        }
                    }

                    else -> Unit
                }
            }
        }
        svm.state.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSharedUiState.MovieId -> setVP()
            }
        }
    }

    private fun setVP() {
        with(binding) {
            pagerAdapter = PagerAdapter(this@MovieDetailsFragment)
            vp2.adapter = pagerAdapter
            requireActivity().changeLanguage(viewModel.getLanguageCode())
            TabLayoutMediator(tabLayout, vp2) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.videos)
                    1 -> tab.text = resources.getString(R.string.more_like_this)
                    2 -> tab.text = resources.getString(R.string.comments)
                    else -> tab.text = resources.getString(R.string.videos)
                }
            }.attach()
            vp2.layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
            vp2.requestLayout()
        }
    }

    private fun setup() {
        with(binding) {
            setRV()
            tvViewMore.setSafeOnClickListener {
                if (textView26.lineCount <= 3) {
                    textView26.maxLines = Int.MAX_VALUE
                    tvViewMore.text = requireActivity().getString(R.string.view_less)
                } else {
                    textView26.maxLines = 3
                    tvViewMore.text = requireActivity().getString(R.string.view_more)
                }
            }


            buttonPlay.setSafeOnClickListener {
                nestedScroll.smoothScrollTo(
                    0,
                    tabLayout.top,
                    2000
                )
                vp2.currentItem = 0
            }

            buttonSeeAllImages.setSafeOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieImagesFragment(
                        args.id
                    )
                )
            }

            buttonFavorite.setSafeOnClickListener {
                if (this@MovieDetailsFragment::favoritesBody.isInitialized) {
                    viewModel.changeFavorite(favoritesBody)
                }
            }

            buttonBack.setSafeOnClickListener { findNavController().popBackStack() }

        }
    }

    private fun setUI() {
        with(binding) {
            textView26.maxLines = 4
            textView26.doOnPreDraw {
                if (textView26.text.toString().isNotEmpty()) {
                    textView26.visible()
                    textView26.doOnPreDraw {
                        if (textView26.lineCount > 3) {
                            tvViewMore.visible()
                            textView26.maxLines = 3
                        } else {
                            tvViewMore.gone()
                        }
                    }
                }
            }
        }
    }

    private fun setRV() {
        with(binding) {
            rvImages.preview(this@MovieDetailsFragment)
            rvActors.adapter = castAdapter
            rvCrew.adapter = crewAdapter
            rvGenres.adapter = genreAdapter
            rvImages.adapter = backdropAdapter


            castAdapter.onClickItem = {

                requireActivity().changeLanguage(viewModel.getLanguageCode())
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToCelebrityDetailsFragment(
                        it.id
                    )
                )
            }

            buttonShareBig.setSafeOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionGlobalShareStoryFragment(
                        Constants.BASE_URL_IMAGE + uiModel.posterPath
                    )
                )
            }

            buttonShareSmall.setSafeOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionGlobalShareStoryFragment(
                        Constants.BASE_URL_IMAGE + uiModel.posterPath
                    )
                )
            }

            backdropAdapter.onClickItem = { item, extras ->
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionGlobalImagePreviewFragment2(
                        uiModel.title, Constants.BASE_URL_IMAGE + item.filePath
                    ),
                    extras
                )

            }


        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vp2.adapter = null
        pd.cancel()
    }

}