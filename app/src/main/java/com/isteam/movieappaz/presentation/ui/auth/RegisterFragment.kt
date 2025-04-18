package com.isteam.movieappaz.presentation.ui.auth

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            val pd = requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is AuthUiState.Error -> {
                        pd.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            it.message,
                            MotionToastStyle.ERROR
                        )
                    }

                    is AuthUiState.RegisterSuccess -> {
                        pd.cancel()
                        if (it.token.isNotEmpty() && it.isFinished) {
                            findNavController().navigate(
                                RegisterFragmentDirections.actionRegisterFragmentToCreateUserFragment(
                                    uuid = it.token,
                                    email = binding.editEmail.text.toString(),
                                    rememberMe = binding.checkRemember.isChecked
                                )
                            )
                            viewModel.changeRegisterFinishedStatus(false)
                        }
                    }

                    AuthUiState.Loading -> pd.show()
                    is AuthUiState.ValidationError -> {
                        pd.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            resources.getString(it.message),
                            MotionToastStyle.ERROR
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        with(binding) {
            cardGoogle.setSafeOnClickListener {
                requireActivity().showMotionToast(
                    resources.getString(R.string.info),
                    resources.getString(R.string.feature_info)
                )
            }
            cardFacebook.setSafeOnClickListener {
                requireActivity().showMotionToast(
                    resources.getString(R.string.info),
                    resources.getString(R.string.feature_info)
                )
            }
            buttonSignIn.setSafeOnClickListener {
                findNavController().popBackStack()
            }
            buttonSignUp.setSafeOnClickListener {
                signUp()
            }
            buttonGoBack.setSafeOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun signUp() {
        with(binding) {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val rememberMe = checkRemember.isChecked

            viewModel.submitRegister(email, password, rememberMe)

        }
    }


}