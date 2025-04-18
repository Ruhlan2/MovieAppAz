package com.isteam.movieappaz.presentation.ui.details.pager

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentMoreLikeThisBinding
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedUiState
import com.isteam.movieappaz.presentation.svm.MovieDetailsSharedViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieDetailsFragmentDirections
import com.isteam.movieappaz.presentation.ui.details.MovieDetailsViewModel
import com.isteam.movieappaz.presentation.ui.details.MovieUiState
import com.isteam.movieappaz.presentation.ui.movieList.adapters.MoviePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class MoreLikeThisFragment :
    BaseFragment<FragmentMoreLikeThisBinding>(FragmentMoreLikeThisBinding::inflate) {
    private val svm by activityViewModels<MovieDetailsSharedViewModel>()

    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val adapter = MoviePagingAdapter()


    override fun onViewCreateFinish() {
        setup()
        requireActivity().changeLanguage(viewModel.getLanguageCode())
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is MovieUiState.Recommendations -> {
                        lifecycleScope.launch {
                            adapter.submitData(it.data)
                        }
                    }
                    else -> {
                        Unit
                    }
                }
            }
        }
        svm.state.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSharedUiState.MovieId -> {
                    viewModel.getMovieRecommendations(it.id)
                    requireActivity().changeLanguage(viewModel.getLanguageCode())
                }
            }
        }
    }

    private fun setup() {
        with(binding) {
            rvMoreLikeThis.adapter = adapter

            adapter.onClickItem = {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionGlobalMovieDetailsFragment(
                        it.id
                    )
                )
            }

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest {
                    when (it.refresh) {
                        LoadState.Loading -> {
                            progressBar.visible()
                        }

                        is LoadState.NotLoading -> {
                            progressBar.gone()
                            if (adapter.itemCount <= 0 && rvMoreLikeThis.isVisible) {
                                rvMoreLikeThis.gone()
                                emptyLayout.visible()
                            } else {
                                emptyLayout.gone()
                            }
                        }

                        is LoadState.Error -> {
                            progressBar.gone()
                            requireActivity().showMotionToast(
                                resources.getString(R.string.error),
                                resources.getString(R.string.error),
                                MotionToastStyle.ERROR
                            )
                        }
                    }
                }
            }

        }
    }
}