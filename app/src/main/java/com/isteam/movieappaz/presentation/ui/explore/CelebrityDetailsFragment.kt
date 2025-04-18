package com.isteam.movieappaz.presentation.ui.explore

import android.animation.LayoutTransition
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentCelebrityDetailsBinding
import com.isteam.movieappaz.presentation.ui.explore.adapters.CelebrityMovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class CelebrityDetailsFragment : BaseFragment<FragmentCelebrityDetailsBinding>(FragmentCelebrityDetailsBinding::inflate) {

    private val movieAdapter = CelebrityMovieAdapter()
    private val viewModel by viewModels<ExploreViewModel>()
    private val args by navArgs<CelebrityDetailsFragmentArgs>()
    override fun onViewCreateFinish() {
        setup()
        viewModel.initialCall(args.castId)

    }

    override fun observeEvents() {
        with(viewModel){
            val pd = requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner){
                when(it){
                    is ExploreUiState.PeopleDetailsList -> {
                        pd.cancel()
                        with(it.list){
                            binding.celebProfile=this
                        }
                    }

                    is ExploreUiState.Loading -> {
                        pd.show()
                    }

                    is ExploreUiState.PeopleMovieCreditsList -> {
                        movieAdapter.submitData(it.list)
                        if (movieAdapter.itemCount<=0) {
                            binding.movieRV.gone()
                            binding.emptyLayout.visible()
                        }else{
                            binding.movieRV.visible()
                            binding.emptyLayout.gone()
                        }
                        pd.cancel()
                    }

                    is ExploreUiState.Error -> {
                        pd.cancel()
                        requireActivity().showMotionToast(resources.getString(R.string.error),it.message,MotionToastStyle.ERROR)
                    }
                    else->Unit
                }
            }
        }
    }

    private fun setup(){
        with(binding){
            movieRV.adapter=movieAdapter

            movieAdapter.onClick={
                    findNavController().navigate(CelebrityDetailsFragmentDirections.actionGlobalMovieDetailsFragment(it.id))
            }

            buttonBack.setSafeOnClickListener {
                findNavController().popBackStack()
            }

            detailCelebrity.setSafeOnClickListener {
                val layoutTransition = constraintNested.layoutTransition
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                if (biography.isGone) biography.visible()
                else biography.gone()
            }
        }

    }
}