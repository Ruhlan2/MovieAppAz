package com.isteam.movieappaz.presentation.ui.auth

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createForgotPassCompleteProgressDialog
import com.isteam.movieappaz.common.utils.createUserProgressDialog
import com.isteam.movieappaz.common.utils.openMailApplication
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentForgotPassBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class ForgotPassFragment :
    BaseFragment<FragmentForgotPassBinding>(FragmentForgotPassBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()

    private val args by navArgs<ForgotPassFragmentArgs>()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            val d1 = requireActivity().createUserProgressDialog(
                R.string.warning,
                R.string.acc_suc,
                R.drawable.shield_icon
            )
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is AuthUiState.Error -> {
                        d1.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            it.message,
                            MotionToastStyle.ERROR
                        )
                    }

                    AuthUiState.Loading -> {
                        d1.show()
                    }

                    is AuthUiState.ValidationError -> {
                        d1.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            resources.getString(it.message),
                            MotionToastStyle.ERROR
                        )
                    }

                    AuthUiState.ForgotPassSuccess -> {
                        d1.cancel()
                        showDialog()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        with(binding) {

            editEmail.setText(args.userEmail)

            buttonGoBack.setSafeOnClickListener {
                findNavController().popBackStack()
            }
            buttonContinue.setSafeOnClickListener {
                forgotPass()
            }
        }
    }

    private fun forgotPass() {
        val email = binding.editEmail.text.toString()

        viewModel.submitForgotPass(email)

    }

    private fun showDialog() {
        lifecycleScope.launch {
            requireActivity().createForgotPassCompleteProgressDialog(
                onClickCheckMail = {
                    findNavController().popBackStack()
                    requireActivity().openMailApplication()
                },
                onClickGoBack = { dialog ->
                    dialog.cancel()
                    findNavController().popBackStack()
                }
            ).show()
        }
    }

}