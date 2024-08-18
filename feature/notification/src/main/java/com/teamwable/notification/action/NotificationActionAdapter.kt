package com.teamwable.notification.action

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NotificationActionModel
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NotificationActionAdapter(
    context: Context,
    private val click: (NotificationActionModel, Int) -> Unit
) : ListAdapter<NotificationActionModel, NotificationActionViewHolder>(
    NotificationActionAdapterDiffCallback,
) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationActionViewHolder {
        val binding = ItemNotificationVpBinding.inflate(inflater, parent, false)
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
