package com.teamwable.notification.information

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NotificationInformationModel
import com.teamwable.notification.CalculateTime
import com.teamwable.notification.R
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.stringOf

class NotificationInformationViewHolder(
    private val binding: ItemNotificationVpBinding,
    private val click: (NotificationInformationModel, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NotificationInformationModel) {
        with(binding) {
            tvNotificationVpContent.apply {
                when (data.infoNotificationType) {
                    "GAMEDONE" -> text = context.stringOf(R.string.tv_notification_information_game_done)
                    "GAMESTART" -> text = context.stringOf(R.string.tv_notification_information_game_start)
                    "WEEKDONE" -> text = context.stringOf(R.string.tv_notification_information_week_done)
                }
            }

            tvNotificationVpTime.text = CalculateTime(root.context).getCalculateTime(data.time)

            root.setOnClickListener {
                click(data, adapterPosition)
            }
        }
    }
}
