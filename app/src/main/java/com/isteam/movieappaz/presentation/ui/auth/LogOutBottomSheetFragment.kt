package com.isteam.movieappaz.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.setSafeOnClickListener
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.databinding.BottomSheetFragmentLogOutBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class LogOutBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding:BottomSheetFragmentLogOutBinding?=null

    private val binding
        get() = requireNotNull(_binding){
            "Binding is null."
        }

    private val viewmodel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFragmentLogOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        with(viewmodel){
            val pd=requireActivity().createProgressDialog()
            state.observe(viewLifecycleOwner){
                when(it){
                    is AuthUiState.SignOutSuccess -> {
                        pd.cancel()
                        findNavController().navigate(LogOutBottomSheetFragmentDirections.actionLogOutBottomSheetFragmentToLoginFragment())
                    }

                    is AuthUiState.Loading -> {
                        pd.show()
                    }

                    is AuthUiState.Error -> {
                        pd.cancel()
                        requireActivity().showMotionToast(
                            resources.getString(R.string.info),
                            it.message,
                            MotionToastStyle.INFO
                        )
                    }

                    else -> Unit
                }
            }
        }

    }


    private fun setListeners(){
        with(binding){
            buttonCancel.setSafeOnClickListener {
                dialog?.dismiss()
            }
            buttonConfirm.setSafeOnClickListener {
               viewmodel.signOutUser()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}