package com.teamwable.notification.information

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NotificationInformationModel
import com.teamwable.notification.R
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.CalculateTime

class NotificationInformationViewHolder(
    private val binding: ItemNotificationVpBinding,
    private val click: (NotificationInformationModel, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: NotificationInformationModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { click(it, adapterPosition) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NotificationInformationModel) {
        item = data

        with(binding) {
            tvNotificationVpContent.apply {
                when (data.infoNotificationType) {
                    "GAMEDONE" -> text = context.stringOf(R.string.tv_notification_information_game_done)
                    "GAMESTART" -> text = context.stringOf(R.string.tv_notification_information_game_start)
                    "WEEKDONE" -> text = context.stringOf(R.string.tv_notification_information_week_done)
                }
            }

            tvNotificationVpTime.text = CalculateTime().getCalculateTime(root.context, data.time)
        }
    }
}
