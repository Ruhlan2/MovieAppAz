package com.isteam.movieappaz.presentation.ui.favorites

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {

    private val viewModel by viewModels<FavoritesViewModel>()
    private val adapter = FavoritesAdapter()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(binding) {
            with(viewModel) {
                val pd = requireActivity().createProgressDialog()
                state.observe(viewLifecycleOwner) {
                    when (it) {
                        is FavoritesUiState.Error -> {
                            pd.cancel()
                            requireActivity().showMotionToast(
                                resources.getString(R.string.error),
                                it.message,
                                MotionToastStyle.ERROR
                            )
                        }

                        is FavoritesUiState.Deleted -> {
                            pd.cancel()
                        }
                        is FavoritesUiState.Favorites -> {
                            pd.cancel()
                            if (it.data.isEmpty()) {
                                emptyLayout.visible()
                                rvFavorites.gone()
                            } else {
                                if (emptyLayout.isVisible) {
                                    emptyLayout.gone()
                                    rvFavorites.visible()
                                }
                                adapter.submitData(it.data)
                            }
                        }

                        FavoritesUiState.Loading -> {
                            pd.show()
                        }

                        FavoritesUiState.OnStopLoading -> pd.cancel()
                    }
                }
            }
        }
    }

    private fun setup() {
        setRV()
    }

    private fun setRV() {
        with(binding) {
            rvFavorites.adapter = adapter

            adapter.onClickItem = {
                findNavController().navigate(
                    FavoritesFragmentDirections.actionGlobalMovieDetailsFragment(
                        it
                    )
                )
            }

            adapter.onClickDeleteButton = {
                viewModel.removeFavorite(it)
            }

        }
    }

    override fun onPause() {
        viewModel.onStopLoading()
        super.onPause()
    }

}