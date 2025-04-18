package com.isteam.movieappaz.presentation.ui.aiShowCase

import android.os.CountDownTimer
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.databinding.FragmentAiShowCaseBinding
import com.isteam.movieappaz.domain.model.AiShowCaseModel
import com.isteam.movieappaz.presentation.ui.movieAI.MovieAiViewModel
import com.isteam.movieappaz.presentation.ui.profile.adapters.AiShowCaseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiShowCaseFragment :
    BaseFragment<FragmentAiShowCaseBinding>(FragmentAiShowCaseBinding::inflate) {

    private val adapter = AiShowCaseAdapter()
    private var countDownTimer: CountDownTimer? = null
    private val args by navArgs<AiShowCaseFragmentArgs>()
    private val chatViewModel by activityViewModels<MovieAiViewModel>()

    private val listOfAiShowCase by lazy {
        listOf(
            AiShowCaseModel(
                1, null, resources.getString(R.string.ai_show_case_1)
            ),
            AiShowCaseModel(
                2, R.raw.ai_anim_1, resources.getString(R.string.ai_show_case_2)
            ),
            AiShowCaseModel(
                3, R.raw.ai_anim_2, resources.getString(R.string.ai_show_case_3)
            ),
            AiShowCaseModel(
                4, null, resources.getString(R.string.ai_powered_title)
            ),
        )
    }

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {

    }

    private fun setup() {
        vpSetup()
        with(binding) {
            buttonNext.setSafeOnClickListener {
                var position = vp2.currentItem
                if (position == listOfAiShowCase.size - 1) {
                    findNavController().popBackStack()
                    if (!args.dontNeedToSave) {
                        chatViewModel.storyDone()
                    }
                } else {
                    vp2.setCurrentItem(++position, true)
                }
            }
        }
    }

    private fun vpSetup() {
        with(binding) {

            adapter.submitData(listOfAiShowCase)
            vp2.adapter = adapter

            vp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (position == listOfAiShowCase.size - 1) {
                        buttonNext.text = resources.getString(R.string.finish)
                    } else {
                        buttonNext.text = resources.getString(R.string.next)
                    }

                    countDownTimer?.cancel()
                    binding.pbAiShow.setProgress(0, true)
                    countDownTimer = object : CountDownTimer(5000L, 100L) {
                        override fun onTick(millisUntilFinished: Long) {
                            binding.pbAiShow.setProgress(
                                (((5000L - millisUntilFinished)) / 45).toInt(),
                                true
                            )
                        }

                        override fun onFinish() {
                            if (position < listOfAiShowCase.size - 1) {
                                vp2.setCurrentItem(position + 1, true)
                            } else {
                                findNavController().popBackStack()
                            }
                        }
                    }
                    countDownTimer?.start()
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }

}