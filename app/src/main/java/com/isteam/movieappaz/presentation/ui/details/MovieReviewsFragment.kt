package com.isteam.movieappaz.presentation.ui.details

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showDialog
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentMovieReviewsBinding
import com.isteam.movieappaz.presentation.ui.details.adapters.ReviewsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment<FragmentMovieReviewsBinding>(
    FragmentMovieReviewsBinding::inflate
) {

    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val adapter = ReviewsPagingAdapter()
    private val args by navArgs<MovieReviewsFragmentArgs>()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is MovieUiState.Reviews -> {
                        lifecycleScope.launch {
                            adapter.submitData(it.data)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        viewModel.getReviews(args.id)
        setRV()
        with(binding) {
            materialToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setRV() {
        with(binding) {
            rvComments.adapter = adapter

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest {
                    when (val cs = it.refresh) {
                        is LoadState.Error -> {
                            linearProgressIndicator.gone()
                            requireActivity().showDialog(
                                resources.getString(R.string.error),
                                cs.error.localizedMessage.orEmpty(),
                                action = {
                                    findNavController().popBackStack()
                                }
                            )
                        }

                        LoadState.Loading -> {
                            linearProgressIndicator.visible()
                        }

                        is LoadState.NotLoading -> {
                            linearProgressIndicator.gone()
                            checkEmptyState()
                        }
                    }
                }
            }
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