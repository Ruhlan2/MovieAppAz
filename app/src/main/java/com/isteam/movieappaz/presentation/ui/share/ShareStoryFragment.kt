package com.isteam.movieappaz.presentation.ui.share

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.saveAsImageToPhone
import com.isteam.movieappaz.databinding.FragmentShareStoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShareStoryFragment :
    BaseFragment<FragmentShareStoryBinding>(FragmentShareStoryBinding::inflate) {

    private var isPermissionGranted = false
    private val viewModel by viewModels<ShareViewModel>()
    private val args by navArgs<ShareStoryFragmentArgs>()

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {

    }

    private fun setup() {
        with(binding) {
            requireActivity().changeLanguage(viewModel.getLanguageCode())
            image = args.imageUrl
            askPermission(true)
            buttonShare.setSafeOnClickListener {
                checkPermission()
            }

            buttonBack.setSafeOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun checkPermission() {
        if (isPermissionGranted) {
            binding.mainConstraint.saveAsImageToPhone(true)
        } else {
            askPermission(false)
        }
    }

    private fun askPermission(isFirst: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if (!isFirst) binding.mainConstraint.saveAsImageToPhone(true)
        } else {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (!it.containsValue(false)) {
                    isPermissionGranted = true
                    if (!isFirst) binding.mainConstraint.saveAsImageToPhone(true)
                } else {
                    val alert = MaterialAlertDialogBuilder(requireContext())
                    alert.setTitle(R.string.warning).setMessage(R.string.permission_storage)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            requireActivity().finish()
                        }.create().show()
                }
            }.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

}