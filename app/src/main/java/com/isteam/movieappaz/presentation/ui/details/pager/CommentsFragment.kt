package com.isteam.movieappaz.presentation.ui.details.pager

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentCommentsBinding
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedUiState
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieDetailsFragmentDirections
import com.isteam.movieappaz.presentation.ui.details.MovieDetailsViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieUiState
import com.isteam.movieappaz.presentation.ui.details.adapters.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class CommentsFragment : BaseFragment<FragmentCommentsBinding>(
    FragmentCommentsBinding::inflate
) {
    private val svm by activityViewModels<MovieDetailsSharedViewModel>()
    private var movieId: Int = -1
    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val adapter = ReviewsAdapter()

    override fun onViewCreateFinish() {
        setup()
        requireActivity().changeLanguage(viewModel.getLanguageCode())
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

                        is MovieUiState.InitialReviews -> {
                            progressBar.gone()
                            adapter.submitData(it.data.detailsResults)
                            checkEmptyState()
                        }

                        MovieUiState.Loading -> progressBar.visible()
                        else->Unit

                    }
                }
            }
        }
        svm.state.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSharedUiState.MovieId -> {
                    viewModel.getMovieReviewsFirstPage(it.id)
                    movieId = it.id
                    requireActivity().changeLanguage(viewModel.getLanguageCode())
                }
            }
        }
    }

    private fun setup() {
        setRV()
        with(binding) {
            buttonSeeAll.setSafeOnClickListener {
                if (movieId != -1) {
                    findNavController().navigate(
                        MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieReviewsFragment(
                            movieId
                        )
                    )
                }
            }
        }
    }

    private fun setRV() {
        with(binding) {
            rvComments.adapter = adapter
        }
    }

    private fun checkEmptyState() {
        with(binding) {
            if (adapter.itemCount <= 0 && rvComments.isVisible) {
                rvComments.gone()
                emptyLayout.visible()
            } else {
                emptyLayout.gone()
            }
        }
    }

}