package com.isteam.movieappaz.presentation.ui.profile

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.databinding.BottomDeleteAccountLayoutBinding
import com.isteam.movieappaz.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

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
                        pd.cancel()
                        val alert = MaterialAlertDialogBuilder(requireContext())
                        alert.setTitle(R.string.warning).setMessage(it.message)
                            .setPositiveButton(R.string.ok) { dialog, _ ->
                                dialog.cancel()
                            }.create().show()
                    }

                    ProfileUiState.Loading -> pd.show()
                    is ProfileUiState.ProfileDetails -> {
                        pd.cancel()
                        binding.data = it.data
                    }

                    is ProfileUiState.DeleteSuccess -> {
                        pd.cancel()
                        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setup() {
        with(binding) {
            tvEditProfile.setSafeOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
            }
            buttonEditPhoto.setSafeOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
            }
            cardAiPowered.setSafeOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAiShowCaseFragment())
            }
            buttonLogOut.setSafeOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLogOutBottomSheetFragment())
            }

            buttonDeleteAccount.setSafeOnClickListener {
                deleteAccount()
            }

            buttonSettings.setSafeOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
            }

            tvPrivacyPolicy.setSafeOnClickListener {
                val openLinkIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY_LINK))
                startActivity(openLinkIntent)
            }

        }
    }

    private fun deleteAccount() {
        val bsd = BottomSheetDialog(requireContext())
        val bsdBinding =
            BottomDeleteAccountLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        bsd.setContentView(bsdBinding.root)

        bsd.show()

        bsdBinding.buttonConfirm.setSafeOnClickListener {
            viewModel.deleteAccount()
            bsd.dismiss()
        }

        bsdBinding.buttonCancel.setSafeOnClickListener {
            bsd.dismiss()
        }


    }

}