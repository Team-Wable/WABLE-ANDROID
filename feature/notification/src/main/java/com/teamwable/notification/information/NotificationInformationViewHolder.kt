package com.teamwable.notification.information

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.notification.NotificationInformationModel
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.CalculateTime

class NotificationInformationViewHolder(
    private val binding: ItemNotificationVpBinding,
    private val click: (NotificationInformationModel, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: NotificationInformationModel

    init {
        binding.root.setOnClickListener {
            if (this::item.isInitialized) click(item, absoluteAdapterPosition)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NotificationInformationModel) {
        item = data

        with(binding) {
            ivNotificationVpProfile.load(data.imageUrl)
            tvNotificationVpContent.apply {
                val notificationType = NotificationInformationType.valueOf(data.infoNotificationType)
                text = context.stringOf(notificationType.content)
            }
            tvNotificationVpTime.text = CalculateTime().getCalculateTime(root.context, data.time)
        }
    }

    companion object {
        fun from(parent: ViewGroup, click: (NotificationInformationModel, Int) -> Unit): NotificationInformationViewHolder =
            NotificationInformationViewHolder(
                ItemNotificationVpBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                click,
            )
    }
}
