package com.teamwable.notification.information

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NotificationInformationModel
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NotificationInformationAdapter(
    context: Context,
    private val click: (NotificationInformationModel, Int) -> Unit
) : ListAdapter<NotificationInformationModel, NotificationInformationViewHolder>(
    NotificationInformationAdapterDiffCallback,
) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationInformationViewHolder {
        val binding = ItemNotificationVpBinding.inflate(inflater, parent, false)
        return NotificationInformationViewHolder(binding, click)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotificationInformationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val NotificationInformationAdapterDiffCallback =
            ItemDiffCallback<NotificationInformationModel>(
                onItemsTheSame = { old, new -> old.time == new.time },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
