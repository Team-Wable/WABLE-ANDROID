package com.teamwable.notification.information

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import com.teamwable.model.notification.NotificationInformationModel
import com.teamwable.ui.extensions.ItemDiffCallback

class NotificationInformationAdapter(
    private val click: (NotificationInformationModel, Int) -> Unit
) : PagingDataAdapter<NotificationInformationModel, NotificationInformationViewHolder>(
    NotificationInformationAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationInformationViewHolder {
        return NotificationInformationViewHolder.from(parent, click)
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
