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
    private val onNotificationClick: (NotificationActionModel, Int) -> Unit,
    private val onProfileClick: (Int) -> Unit,
) : PagingDataAdapter<NotificationActionModel, NotificationActionViewHolder>(
    NotificationActionAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationActionViewHolder {
        return NotificationActionViewHolder.from(parent, onNotificationClick, onProfileClick)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotificationActionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val NotificationActionAdapterDiffCallback =
            ItemDiffCallback<NotificationActionModel>(
                onItemsTheSame = { old, new -> old.notificationId == new.notificationId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
