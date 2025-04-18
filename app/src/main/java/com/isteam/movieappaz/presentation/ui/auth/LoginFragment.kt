package com.isteam.movieappaz.presentation.ui.auth

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.createVerificationDialog
import com.isteam.movieappaz.common.utils.openMailApplication
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()

    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode==Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            viewModel.handleSignInResults(task)
        }
    }

    override fun onViewCreateFinish() {
        viewModel.getIsRegisterFinished()
        setup()
    }
    override fun observeEvents() {
        with(viewModel) {
            val pd = requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is AuthUiState.IsRegisterFinished -> {
                        if (!it.isFinished) {
                            viewModel.deleteUser()
                        }
                    }
                    is AuthUiState.Error -> {
                        pd.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            it.message,
                            MotionToastStyle.ERROR
                        )
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

                    is AuthUiState.LoginSuccess -> {
                        viewModel.isEmailVerified(it.token)
                    }

                    is AuthUiState.IsVerified -> {
                        if (it.isVerified) {
                            pd.cancel()
                            viewModel.setToken(it.token, binding.checkRemember.isChecked)
                            navigateToHome()
                        } else {
                            pd.cancel()
                            viewModel.sendVerification()
                        }
                    }

                    AuthUiState.VerificationSent -> {
                        pd.cancel()
                        requireActivity().createVerificationDialog(
                            onClickGoBack = {
                                it.cancel()
                            },
                            onClickCheckMail = {
                                requireActivity().openMailApplication()
                            }
                        )
                    }

                    is AuthUiState.DeleteSuccess -> {
                        pd.cancel()
                    }

                    is AuthUiState.GoogleSignInSuccess->{
                        pd.cancel()
                        viewModel.setToken(it.token, true)
                        navigateToHome()
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
                login()
            }
            buttonSignUp.setSafeOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            buttonForgotPass.setSafeOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToForgotPassFragment(
                        editEmail.text.toString()
                    )
                )
            }
        }
    }

    private fun navigateToHome(){
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
    private fun launchSignInWithGoogle(){
        viewModel.launch(launcher)
    }

    private fun login() {
        with(binding) {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val rememberMe = checkRemember.isChecked

            viewModel.submitLogin(email, password, rememberMe)

        }
    }


}