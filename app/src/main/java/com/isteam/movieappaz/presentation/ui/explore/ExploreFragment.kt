package com.isteam.movieappaz.presentation.ui.explore


import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.enableTransitionAnimation
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.preview
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentExploreBinding
import com.isteam.movieappaz.presentation.ui.explore.adapters.CelebrityPagingAdapter
import com.isteam.movieappaz.presentation.ui.explore.adapters.GenreAdapter
import com.isteam.movieappaz.presentation.ui.movieList.adapters.MoviePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>(
    FragmentExploreBinding::inflate
) {

    private val explorePagingAdapter by lazy { MoviePagingAdapter() }
    private val celebPagingAdapter by lazy { CelebrityPagingAdapter() }
    private val recommendedMovieAdapter by lazy { MoviePagingAdapter() }
    private val genreAdapter by lazy { GenreAdapter() }
    private val viewModel by viewModels<ExploreViewModel>()
    private var job: Job? = null

    override fun onViewCreateFinish() {
        postponeEnterTransition()
        viewModel.getExploreData()
        setRecycler()
        searchInputListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableTransitionAnimation()
        super.onCreate(savedInstanceState)
    }

    override fun observeEvents() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ExploreUiState.ExploreMovieList -> {
                    lifecycleScope.launch {
                        explorePagingAdapter.submitData(it.list)
                    }
                }

                is ExploreUiState.Genres -> {
                    genreAdapter.submitData(it.list)
                    if (it.isSelected) {
                        if (binding.textInputLayout4.isVisible) {
                            binding.textInputLayout4.gone()
                            binding.commonExploreLayout.gone()
                            binding.exploreLayout.visible()
                        }
                    } else {
                        binding.textInputLayout4.visible()
                        binding.commonExploreLayout.visible()
                        binding.exploreLayout.gone()
                    }
                }

                is ExploreUiState.ExploreCelebrityList -> {
                    lifecycleScope.launch {
                        celebPagingAdapter.submitData(it.list)
                    }
                }

                is ExploreUiState.RecommendedMovieList -> {
                    lifecycleScope.launch {
                        recommendedMovieAdapter.submitData(it.list)
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun searchInputListener() {
        with(binding) {
            viewModel.getMovies()
            editSearchExplore.addTextChangedListener {
                val query = it?.toString()
                job?.cancel()
                if (!query.isNullOrEmpty()) {
                    job = lifecycleScope.launch {
                        delay(400)
                        if (commonExploreLayout.isVisible) {
                            rvGenres.gone()
                            commonExploreLayout.gone()
                            exploreLayout.visible()
                        }
                        viewModel.searchMovies(query)
                    }
                } else {
                    if (commonExploreLayout.isGone) {
                        commonExploreLayout.visible()
                        exploreLayout.gone()
                        rvGenres.visible()
                        emptyLayout.gone()
                    }
                }
            }
        }

    }

    private fun setRecycler() {
        with(binding) {
            rvExplore.adapter = explorePagingAdapter
            rvGenres.adapter = genreAdapter
            celebrityRv.adapter = celebPagingAdapter
            recommendedMovieRv.adapter = recommendedMovieAdapter

            genreAdapter.onClick = {
                viewModel.changeGenre(it)
                viewModel.discoverGenre(it.id)
            }

            celebPagingAdapter.onClick = {
                findNavController().navigate(
                    ExploreFragmentDirections.actionExploreFragmentToCelebrityDetailsFragment(
                        it.id
                    )
                )
            }

            rvExplore.preview(this@ExploreFragment)

            explorePagingAdapter.onClickItem = {
                findNavController().navigate(
                    ExploreFragmentDirections.actionGlobalMovieDetailsFragment(it.id)
                )
            }

            explorePagingAdapter.onLongClickItem = { model, extra ->
                findNavController().navigate(
                    ExploreFragmentDirections.actionGlobalImagePreviewFragment2(
                        model.title,
                        model.image
                    )
                )
            }

            recommendedMovieAdapter.onClickItem = {
                findNavController().navigate(
                    ExploreFragmentDirections.actionGlobalMovieDetailsFragment(it.id)
                )
            }
            recommendedMovieAdapter.onLongClickItem = { model, extras ->
                findNavController().navigate(
                    ExploreFragmentDirections.actionGlobalImagePreviewFragment2(
                        model.title,
                        model.image
                    )
                )
            }



            recommendedMovieRv.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recommendedMovieRv.layoutManager as GridLayoutManager

                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (position <= 3 && dy < 0) {
                        if (celebrityRv.isGone) {
                            textView42.visible()
                            celebrityRv.visible()
                        }
                    } else if (position > 3 && dy > 0) {
                        if (celebrityRv.isVisible) {
                            textView42.gone()
                            celebrityRv.gone()
                        }
                    }
                }
            })

            lifecycleScope.launch {
                explorePagingAdapter.loadStateFlow.collectLatest { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> {
                            if (!progressBar.isVisible) {
                                progressBar.visible()
                                emptyLayout.gone()
                                exploreLayout.gone()
                            }
                        }

                        is LoadState.NotLoading -> {
                            progressBar.gone()
                            if (explorePagingAdapter.itemCount <= 0 && !commonExploreLayout.isVisible) {
                                emptyLayout.visible()
                                exploreLayout.gone()
                            } else {
                                emptyLayout.gone()
                                if (commonExploreLayout.isVisible) {
                                    exploreLayout.gone()
                                } else {
                                    exploreLayout.visible()
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
    }


}
