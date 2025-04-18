package com.isteam.movieappaz.presentation.ui.movieList.preview

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.enableTransitionAnimation
import com.isteam.movieappaz.common.utils.saveAsImageToPhone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.FragmentImagePreviewBinding
import www.sanju.motiontoast.MotionToastStyle
import java.util.concurrent.TimeUnit

class ImagePreviewFragment :
    BaseFragment<FragmentImagePreviewBinding>(FragmentImagePreviewBinding::inflate) {

    private val args by navArgs<ImagePreviewFragmentArgs>()
    private var isPermissionGranted = false

    override fun onViewCreateFinish() {
        setup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableTransitionAnimation()
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)
    }

    override fun observeEvents() {

    }

    private fun setup() {
        with(binding) {
            imageUrl = args.imageUrl
            title = args.title
            askPermission(isFirst = true, isSharing = false)
            materialToolbar4.setNavigationOnClickListener { findNavController().popBackStack() }

            buttonSave.setSafeOnClickListener {
                checkPermission(false, isSharing = true)
            }

            buttonDownload.setSafeOnClickListener {
                checkPermission(false, isSharing = false)
            }

        }

    }

    private fun checkPermission(isFirst: Boolean, isSharing: Boolean) {
        if (isPermissionGranted) {
            binding.constraintImage.saveAsImageToPhone(isSharing)
            requireActivity().showMotionToast(
                resources.getString(R.string.suc),
                resources.getString(R.string.photo_save_suc),
                MotionToastStyle.SUCCESS
            )
        } else {
            askPermission(isFirst, isSharing)
        }
    }

    private fun askPermission(isFirst: Boolean, isSharing: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if (!isFirst) binding.constraintImage.saveAsImageToPhone(isSharing)
        } else {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (!it.containsValue(false)) {
                    isPermissionGranted = true
                    if (!isFirst) binding.constraintImage.saveAsImageToPhone(isSharing)
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