package com.isteam.movieappaz.presentation.ui.auth

import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.createVerificationDialog
import com.isteam.movieappaz.common.utils.openMailApplication
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentCreateUserBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle
import java.util.UUID

@AndroidEntryPoint
class CreateUserFragment :
    BaseFragment<FragmentCreateUserBinding>(FragmentCreateUserBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()
    private val args by navArgs<CreateUserFragmentArgs>()

    private lateinit var photoPick: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoPick = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.imageUser.setImageURI(uri)
                selectedImage = uri
            } else {
                requireActivity().showMotionToast(
                    resources.getString(R.string.info),
                    resources.getString(R.string.not_selected_photo)
                )
            }
        }
    }

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

                    is AuthUiState.ValidationError -> {
                        pd.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            resources.getString(it.message),
                            MotionToastStyle.ERROR
                        )
                    }

                    is AuthUiState.CreateUserSuccess -> {
                        pd.cancel()
                        viewModel.sendVerification()
                    }

                    is AuthUiState.VerificationSent -> {
                        pd.cancel()
                        viewModel.changeRegisterFinishedStatus(true)
                        requireActivity().createVerificationDialog(
                            onClickCheckMail = {
                                it.cancel()
                                findNavController().navigate(CreateUserFragmentDirections.actionCreateUserFragmentToLoginFragment())
                                requireActivity().openMailApplication()
                            },
                            onClickGoBack = {
                                it.cancel()
                                findNavController().navigate(CreateUserFragmentDirections.actionCreateUserFragmentToLoginFragment())
                            }
                        )
                    }

                    is AuthUiState.DeleteSuccess -> {
                        pd.cancel()
                        findNavController().popBackStack()
                    }

                    AuthUiState.Loading -> {
                        pd.show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.deleteUser()
        }
        with(binding) {
            buttonContinue.setSafeOnClickListener {
                createUser(false)
            }
            buttonSkip.setSafeOnClickListener {
                createUser(true)
            }
            buttonGoBack.setSafeOnClickListener {
                viewModel.deleteUser()
            }
            buttonEditImage.setSafeOnClickListener {
                photoPick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }


    private fun createUser(isSkipped: Boolean) {
        with(binding) {
            if (isSkipped) {
                viewModel.createUser(
                    uuid = args.uuid,
                    email = args.email,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    null,
                    rememberMe = args.rememberMe
                )
            } else {
                val fullName = editFullName.text.toString()
                val nickname = editNickname.text.toString()

                viewModel.createUser(
                    uuid = args.uuid,
                    email = args.email,
                    nickname,
                    fullName,
                    selectedImage,
                    args.rememberMe
                )
            }
        }
    }

}