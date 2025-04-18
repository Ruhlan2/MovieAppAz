package com.isteam.movieappaz.presentation.ui.splash

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.exitTheApplication
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel by viewModels<SplashViewModel>()


    override fun onViewCreateFinish() {
        viewModel.getInitial()
    }


    override fun observeEvents() {
        with(viewModel) {
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is SplashUiState.Initial -> {
                        requireActivity().changeLanguage(it.languageCode)
                        setCredentials(it.isFinished, it.token, it.remember)
                    }

                    is SplashUiState.InternetConnection -> {
                       setConnection(it.connection)
                    }

                    is SplashUiState.InternetError -> {
                        requireActivity().showMotionToast(
                            getString(R.string.error),
                            it.error,
                            MotionToastStyle.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun setConnection(isConnected:Boolean) {
        with(binding){
            lifecycleScope.launch {
                startAnimation()
                delay(2000)
                startTextAnimation()
                delay(2000)
                if (isConnected){
                    viewModel.getCredentials()
                }else{
                    if (splashLayout.isVisible){
                        unavailableLyt.visible()
                        splashLayout.gone()
                    }
                    exitTheApplication(root){
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun startAnimation() {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.logo_animation)
        binding.cardLogo.startAnimation(anim)
    }

    private fun startTextAnimation() {
        val animText = AnimationUtils.loadAnimation(requireContext(), R.anim.left_to_right_anim)
        binding.textView.visible()
        binding.textView.startAnimation(animText)
    }

    private fun setCredentials(isFinished: Boolean, token: String, remember: Boolean){
        with(binding) {
            unavailableLyt.gone()
            splashLayout.visible()
            if (isFinished && token.isNotEmpty() && remember) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
            } else if (isFinished && !remember) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            } else {
                requireActivity().changeLanguage(LanguageCode.AZ)
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingLanguageFragment())
            }
        }
    }

}