package com.teamwable.notification.information

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import com.teamwable.model.notification.NotificationInformationModel
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NotificationInformationAdapter(
    private val click: (NotificationInformationModel, Int) -> Unit
) : PagingDataAdapter<NotificationInformationModel, NotificationInformationViewHolder>(
    NotificationInformationAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationInformationViewHolder {
        val binding = ItemNotificationVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationInformationViewHolder(binding, click)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotificationInformationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val NotificationInformationAdapterDiffCallback =
            ItemDiffCallback<NotificationInformationModel>(
                onItemsTheSame = { old, new -> old.infoNotificationId == new.infoNotificationId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
