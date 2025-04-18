package com.isteam.movieappaz.presentation.ui.onboarding

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentOnboardingBinding
import com.isteam.movieappaz.domain.model.OnboardingUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    private val viewModel by viewModels<OnboardingViewModel>()
    private val adapter = OnboardingAdapter()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    OnboardingUiState.Finish -> findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
                    )

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        setVP()
        with(binding) {
            buttonContinue.setSafeOnClickListener {
                var position = viewPager2.currentItem
                if (position == 2) {
                    viewModel.setOnboardingFinished()
                } else {
                    viewPager2.setCurrentItem(++position, true)
                }
            }
            buttonSkip.setSafeOnClickListener {
                viewModel.setOnboardingFinished()
            }
        }

    }

    private fun setVP() {
        with(binding) {
            val data = listOf(
                OnboardingUiModel(
                    1,
                    R.drawable.ob_1,
                    R.string.onboarding_screen_text_1,
                    R.string.onboarding_screen_subtext_1
                ),
                OnboardingUiModel(
                    2,
                    R.drawable.ob_2,
                    R.string.onboarding_screen_text_2,
                    R.string.onboarding_screen_subtext_2
                ),
                OnboardingUiModel(
                    3,
                    R.drawable.ob_3,
                    R.string.onboarding_screen_text_3,
                    R.string.onboarding_screen_subtext_3
                ),
            )

            adapter.submitData(data)

            viewPager2.adapter = adapter
            wormDotsIndicator.attachTo(viewPager2)

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position == 2) {
                        buttonSkip.gone()
                        buttonContinue.text = resources.getString(R.string.get_started)
                    } else {
                        if (!buttonSkip.isVisible) {
                            buttonSkip.visible()
                            buttonContinue.text = resources.getString(R.string.continu_e)
                        }
                    }
                }
            })
        }

    }

}