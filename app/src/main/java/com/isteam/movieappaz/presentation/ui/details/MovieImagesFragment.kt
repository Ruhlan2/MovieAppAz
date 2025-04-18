package com.isteam.movieappaz.presentation.ui.details

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.enableTransitionAnimation
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.preview
import com.isteam.movieappaz.common.utils.showDialog
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentMovieImagesBinding
import com.isteam.movieappaz.presentation.ui.details.adapters.BackdropsGridAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieImagesFragment :
    BaseFragment<FragmentMovieImagesBinding>(FragmentMovieImagesBinding::inflate) {

    private val viewModel by viewModels<MovieDetailsViewModel>()
    private val adapter = BackdropsGridAdapter()
    private val args by navArgs<MovieImagesFragmentArgs>()

    override fun onViewCreateFinish() {
        postponeEnterTransition()
        setup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableTransitionAnimation()
    }

    override fun observeEvents() {
        with(viewModel) {
            val pd = requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is MovieUiState.Error -> {
                        pd.cancel()
                        requireActivity().showDialog(
                            resources.getString(R.string.error),
                            it.message,
                            action = {
                                findNavController().popBackStack()
                            }
                        )
                    }

                    is MovieUiState.Images -> {
                        pd.cancel()
                        adapter.submitData(it.data)
                        checkEmptyState()
                    }

                    MovieUiState.Loading -> {
                        pd.show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        setRV()
        viewModel.getAllImages(args.id)
        with(binding) {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            adapter.onLongClickItem = { model, extras ->
                findNavController().navigate(
                    MovieImagesFragmentDirections.actionGlobalImagePreviewFragment2(
                        "",
                        model.filePath
                    )
                )
            }

        }
    }

    private fun setRV() {
        with(binding) {
            rvImages.preview(this@MovieImagesFragment)
            rvImages.adapter = adapter
        }
    }

    private fun checkEmptyState() {
        with(binding) {
            if (adapter.itemCount <= 0 && rvImages.isVisible) {
                rvImages.gone()
                emptyLayout.visible()
            } else {
                emptyLayout.gone()
            }
        }
    }


}