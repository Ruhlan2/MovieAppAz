package com.isteam.movieappaz.presentation.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isteam.movieappaz.common.base.BaseAdapter
import com.isteam.movieappaz.databinding.ItemNotificationsBinding
import com.isteam.movieappaz.domain.model.notifications.NotificationUiModel

class NotificationsAdapter : BaseAdapter<NotificationUiModel>() {

    inner class NotificationViewHolder(private val binding: ItemNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(item: NotificationUiModel) {
            with(binding) {
                data = item
                executePendingBindings()
            }
        }
    }

    override fun onCreate(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder =
        NotificationViewHolder(ItemNotificationsBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationViewHolder -> {
                items.getOrNull(position)?.let { item -> holder.find(item) }
            }
        }
    }


}