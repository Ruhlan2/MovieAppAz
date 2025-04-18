package com.isteam.movieappaz.presentation.ui.profile

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.ivProfile.setImageURI(uri)
                selectedImage = uri
            } else {
                requireActivity().showMotionToast(
                    resources.getString(R.string.info),
                    resources.getString(R.string.not_selected_photo),
                    MotionToastStyle.INFO
                )
            }
        }
    }

    override fun onViewCreateFinish() {
        viewModel.getUserDetails()
        setup()
    }

    override fun observeEvents() {
        with(viewModel) {
            val pd = requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner) {
                when (it) {
                    is ProfileUiState.Error -> {
                        pd.show()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.error),
                            it.message,
                            MotionToastStyle.ERROR
                        )
                    }

                    ProfileUiState.Loading -> pd.show()
                    is ProfileUiState.ProfileDetails -> {
                        pd.cancel()
                        binding.data = it.data
                    }

                    is ProfileUiState.EditProfileSuccess -> {
                        pd.cancel()
                        requireActivity().showMotionToast(getString(R.string.info),getString(R.string.suc),MotionToastStyle.SUCCESS)
                        findNavController().popBackStack()
                    }

                    is ProfileUiState.EditProfileError -> {
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
            buttonBack.setSafeOnClickListener {
                findNavController().popBackStack()
            }

            buttonEditPhoto.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            buttonEmpty.setSafeOnClickListener {
                viewModel.submitEdit(
                    etFullName.text.toString(),
                    etNickname.text.toString(),
                    selectedImage
                )
            }
        }
    }

}