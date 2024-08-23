package com.teamwable.notification.action

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import com.teamwable.model.notification.NotificationActionModel
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NotificationActionAdapter(
    private val click: (NotificationActionModel, Int) -> Unit
) : PagingDataAdapter<NotificationActionModel, NotificationActionViewHolder>(
    NotificationActionAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationActionViewHolder {
        val binding = ItemNotificationVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationActionViewHolder(binding, click)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotificationActionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val NotificationActionAdapterDiffCallback =
            ItemDiffCallback<NotificationActionModel>(
                onItemsTheSame = { old, new -> old.notificationId == new.notificationId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
