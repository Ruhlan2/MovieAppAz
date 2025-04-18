package com.isteam.movieappaz.presentation.ui.notifications

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseFragment
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showDialog
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment :
    BaseFragment<FragmentNotificationsBinding>(FragmentNotificationsBinding::inflate) {

    private val viewModel by viewModels<NotificationViewModel>()
    private val adapter by lazy { NotificationsAdapter() }

    override fun onViewCreateFinish() {
        setup()
    }

    override fun observeEvents() {
        with(binding) {
            with(viewModel) {
                state.observe(viewLifecycleOwner) {
                    when (it) {
                        is NotificationsUiState.Error -> {
                            progressBar.gone()
                            requireActivity().showDialog(
                                resources.getString(R.string.error),
                                it.message,
                                action = {
                                    findNavController().popBackStack()
                                }
                            )
                        }

                        NotificationsUiState.Loading -> {
                            progressBar.visible()
                            emptyLayout.gone()
                            rvNotifications.gone()
                        }

                        is NotificationsUiState.Success -> {
                            progressBar.gone()
                            if (it.notifications.isEmpty()) {
                                rvNotifications.gone()
                                emptyLayout.visible()
                            } else {
                                rvNotifications.visible()
                                emptyLayout.gone()
                                adapter.submitData(it.notifications)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setup() {
        setRv()
        with(binding) {
            materialToolbar3.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setRv() {
        with(binding) {
            rvNotifications.adapter = adapter
        }
    }

}