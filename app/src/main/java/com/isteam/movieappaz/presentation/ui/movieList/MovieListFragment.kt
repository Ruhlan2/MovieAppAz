package com.isteam.movieappaz.presentation.ui.movieList

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.databinding.FragmentMovieListBinding
import com.isteam.movieappaz.presentation.ui.movieList.adapters.MoviePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>(
    FragmentMovieListBinding::inflate
) {
    private val viewModel by viewModels<MovieListViewModel>()
    private val movieListPagingAdapter = MoviePagingAdapter()
    private val args: MovieListFragmentArgs by navArgs()

    override fun onViewCreateFinish() {
        setRecycler()
        viewModel.getMovieListData(args.movieType)
        binding.textMovieListTitle.text = resources.getString(
            when (args.movieType) {
                MovieTypeEnum.POPULAR_MOVIES -> R.string.top_movies
                MovieTypeEnum.NOW_PLAYING -> R.string.now_playing
                MovieTypeEnum.UPCOMING -> R.string.coming_soon
                else -> R.string.top_movies
            }
        )
        binding.buttonNavigationMovieList.setSafeOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observeEvents() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MovieListUiState.PagingMovieList -> {
                    lifecycleScope.launch {
                        movieListPagingAdapter.submitData(it.list)
                    }
                }

                else -> Unit
            }
        }
    }

    private fun setRecycler() {
        binding.rvMovieList.adapter = movieListPagingAdapter

        movieListPagingAdapter.onClickItem = {
            findNavController().navigate(
                MovieListFragmentDirections
                    .actionGlobalMovieDetailsFragment(
                        it.id
                    )
            )
        }

        val pd = requireActivity().createProgressDialog()

        lifecycleScope.launch {
            movieListPagingAdapter.loadStateFlow.collectLatest { state ->
                when (state.refresh) {
                    is LoadState.Loading -> pd.show()

                    is LoadState.NotLoading -> pd.cancel()

                    else -> {}
                }
            }
        }
    }

}