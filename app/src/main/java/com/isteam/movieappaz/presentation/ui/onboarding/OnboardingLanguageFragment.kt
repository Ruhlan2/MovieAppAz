package com.isteam.movieappaz.presentation.ui.onboarding

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.restartActivityInLanguage
import com.isteam.movieappaz.databinding.FragmentOnboardingLanguageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingLanguageFragment :
    BaseFragment<FragmentOnboardingLanguageBinding>(FragmentOnboardingLanguageBinding::inflate) {

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is OnboardingUiState.Language -> {
                        binding.chipGroup.check(
                            when (it.languageCode) {
                                LanguageCode.AZ -> R.id.chipAz
                                LanguageCode.EN -> R.id.chipEn
                                LanguageCode.RU -> R.id.chipRu
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        with(binding) {
            viewModel.getLanguageCode()
            chipAz.setSafeOnClickListener {
                viewModel.setLanguageCode(LanguageCode.AZ)
                requireActivity().restartActivityInLanguage(LanguageCode.AZ)
            }
            chipEn.setSafeOnClickListener {
                viewModel.setLanguageCode(LanguageCode.EN)
                requireActivity().restartActivityInLanguage(LanguageCode.EN)
            }

            chipRu.setSafeOnClickListener {
                viewModel.setLanguageCode(LanguageCode.RU)
                requireActivity().restartActivityInLanguage(LanguageCode.RU)
            }

            buttonContinue.setSafeOnClickListener {
                requireActivity().changeLanguage(
                    when (chipGroup.checkedChipId) {
                        R.id.chipAz -> LanguageCode.AZ
                        R.id.chipEn -> LanguageCode.EN
                        R.id.chipRu -> LanguageCode.RU
                        else -> LanguageCode.AZ
                    }
                )
                findNavController().navigate(OnboardingLanguageFragmentDirections.actionOnboardingLanguageFragmentToOnboardingFragment())
            }
        }
    }


}