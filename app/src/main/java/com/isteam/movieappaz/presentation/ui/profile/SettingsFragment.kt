package com.isteam.movieappaz.presentation.ui.profile

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.ThemeEnum
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.onBackPress
import com.isteam.movieappaz.common.utils.restartActivityInLanguage
import com.isteam.movieappaz.databinding.BottomLanguageLayoutBinding
import com.isteam.movieappaz.databinding.FragmentSettingsBinding
import com.isteam.movieappaz.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val svm by activityViewModels<MainViewModel>()
    private var languageCode = LanguageCode.AZ
    private var isDarkMode = false
    private var isThemeSelected = false

    override fun onViewCreateFinish() {
        setup()
        onBackPress {
            requireActivity().changeLanguage(languageCode)
            svm.getLanguage()
            findNavController().popBackStack()
        }
    }

    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is ProfileUiState.Language -> {
                        languageCode = it.languageCode
                        when (languageCode) {
                            LanguageCode.EN -> binding.tvLanguage.setText(R.string.en_flag)
                            LanguageCode.AZ -> binding.tvLanguage.setText(R.string.az_flag)
                            LanguageCode.RU -> binding.tvLanguage.setText(R.string.ru_flag)
                        }
                        requireActivity().changeLanguage(languageCode)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        viewModel.getLanguageCode()
        with(binding) {
            val themeParameters = viewModel.getThemeParameters()
            isThemeSelected = themeParameters.isThemeSelected
            binding.buttonDarkMode.isChecked = if (!isThemeSelected) {
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            } else {
                isDarkMode = themeParameters.theme == ThemeEnum.DARK_MODE
                themeParameters.theme == ThemeEnum.DARK_MODE
            }

            toolbar.setNavigationOnClickListener {
                requireActivity().changeLanguage(languageCode)
                svm.getLanguage()
                findNavController().popBackStack()
            }

            buttonDarkMode.setOnCheckedChangeListener { buttonView, _ ->
                if (!isThemeSelected) viewModel.setIsThemeSelectedFromApp(true)
                setDarkMode()
            }

            linearLayout2.setSafeOnClickListener {
                provideLanguage()
            }

        }
    }

    private fun setDarkMode() {
        if (isThemeSelected) {
            if (isDarkMode) {
                viewModel.setTheme(ThemeEnum.LIGHT_MODE)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                viewModel.setTheme(ThemeEnum.DARK_MODE)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        } else {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        requireActivity().recreate()
    }

    private fun provideLanguage() {
        val bsd = BottomSheetDialog(requireContext())
        val bottomBinding =
            BottomLanguageLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        bsd.setContentView(bottomBinding.root)

        when (languageCode) {
            LanguageCode.EN -> bottomBinding.radioEn.isChecked = true
            LanguageCode.AZ -> bottomBinding.radioAz.isChecked = true
            LanguageCode.RU -> bottomBinding.radioRu.isChecked = true
        }

        with(bottomBinding) {
            radioAz.setSafeOnClickListener {
                bsd.dismiss()
                changeLanguage(LanguageCode.AZ)
            }

            radioEn.setSafeOnClickListener {
                bsd.dismiss()
                changeLanguage(LanguageCode.EN)

            }

            radioRu.setSafeOnClickListener {
                bsd.dismiss()
                changeLanguage(LanguageCode.RU)
            }
        }

        bsd.show()

    }

    private fun changeLanguage(newLanguage: LanguageCode) {
        viewModel.setLanguageCode(
            newLanguage
        )
        requireActivity().restartActivityInLanguage(newLanguage)
    }


}