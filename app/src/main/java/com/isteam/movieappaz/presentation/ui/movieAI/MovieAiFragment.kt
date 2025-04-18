package com.isteam.movieappaz.presentation.ui.movieAI

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.ManualResourceProvider
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentMovieAiBinding
import com.isteam.movieappaz.presentation.ui.movieAI.adapters.MovieChatAdapter
import com.isteam.movieappaz.presentation.ui.movieAI.adapters.MovieMessageAdapter
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class MovieAiFragment : BaseFragment<FragmentMovieAiBinding>(FragmentMovieAiBinding::inflate) {

    private val viewModel by activityViewModels<MovieAiViewModel>()
    private val adapter = MovieChatAdapter()
    private val messageAdapter = MovieMessageAdapter()

    override fun onViewCreateFinish() {
        viewModel.setManualResourceProvider(ManualResourceProvider(requireContext()))
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            with(binding) {
                state.observe(viewLifecycleOwner) {
                    when (it) {
                        is MovieAiUiState.AiMessage -> {
                            adapter.submitData(it.data)
                            loadingChatConstraint.gone()
                            scrollToBottom()
                        }

                        is MovieAiUiState.Error -> {
                            requireActivity().showMotionToast(
                                resources.getString(R.string.error),
                                it.message,
                                MotionToastStyle.ERROR
                            )
                            rvMessage.visible()
                            loadingChatConstraint.gone()
                        }

                        MovieAiUiState.Loading -> {
                            rvMessage.gone()
                            loadingChatConstraint.visible()
                        }

                        is MovieAiUiState.MessageList -> {
                            messageAdapter.submitData(it.messages)
                            rvMessage.visible()
                            loadingChatConstraint.gone()
                        }

                        MovieAiUiState.MessageComplete -> {
                            changeMessageList()
                        }

                        is MovieAiUiState.UserMessage -> {
                            adapter.submitData(it.data)
                        }

                        MovieAiUiState.StoryDone -> viewModel.startConversation()
                        MovieAiUiState.StoryShow -> findNavController().navigate(
                            MovieAiFragmentDirections.actionMovieAiFragmentToAiShowCaseFragment(
                                false
                            )
                        )

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setup() {
        with(binding) {
            setRV()
        }
    }

    private fun setRV() {
        with(binding) {
            rvChat.adapter = adapter

            adapter.submitData(viewModel.getChat())
            scrollToBottom()


            rvMessage.layoutManager =
                FlexboxLayoutManager(requireContext(), FlexDirection.ROW_REVERSE, FlexWrap.WRAP)
            rvMessage.adapter = messageAdapter


            messageAdapter.onClickItem = {
                viewModel.generateContent(it.message)
            }

        }
    }

    private fun scrollToBottom() {
        with(binding) {
            scrollView.smoothScrollTo(0, constraintMain.height, 1000)
        }
    }


}