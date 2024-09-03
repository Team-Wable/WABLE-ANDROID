package com.teamwable.notification.action

import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.notification.NotificationActionModel
import com.teamwable.notification.R
import com.teamwable.notification.databinding.ItemNotificationVpBinding
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.CalculateTime
import timber.log.Timber

class NotificationActionViewHolder(
    private val binding: ItemNotificationVpBinding,
    private val onNotificationClick: (NotificationActionModel, Int) -> Unit,
    private val onProfileClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: NotificationActionModel
    private val dummyUserName = "차은우"

    init {
        binding.root.setOnClickListener {
            onNotificationClick(item, adapterPosition)
        }

        binding.ivNotificationVpProfile.setOnClickListener {
            onProfileClick(item.triggerMemberId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NotificationActionModel) {
        with(binding) {
            item = data

            val spannableText = when (data.notificationTriggerType) {
                "contentLiked" -> {
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        R.string.tv_notification_action_content_liked,
                        data = data
                    )
                }

                "comment" -> {
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        R.string.tv_notification_action_feed_comment,
                        data = data
                    )
                }

                "commentLiked" -> {
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        R.string.tv_notification_action_comment_liked,
                        data = data
                    )
                }

                "actingContinue" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_acting_continue,
                    ACTING_CONTINUE_LEN,
                    data = data
                )

                "beGhost" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_be_ghost,
                    data = data
                )

                "contentGhost" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_content_ghost,
                    data = data
                )

                "commentGhost" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_comment_ghost,
                    data = data
                )

                "userBan" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_user_ban,
                    data = data
                )

                "popularWriter" -> getSpannableStyle(
                    data.memberNickname,
                    R.string.tv_notification_action_popular_writer,
                    data = data
                )

                "popularContent" -> getSpannablePopularText(
                    binding.root.context.getString(R.string.tv_notification_action_popular_content),
                    data = data
                )

                else -> {
                    Timber.tag("notification_action").e("등록되지 않은 노티가 감지되었습니다 : ${data.notificationTriggerType}")
                    SpannableString("")
                }
            }

            ivNotificationVpProfile.load(data.triggerMemberProfileUrl)
            tvNotificationVpContent.text = spannableText
            tvNotificationVpTime.text = CalculateTime().getCalculateTime(root.context, data.time)
        }
    }

    private fun clickableSpan(data: NotificationActionModel, isNickname: Boolean) = object : ClickableSpan() {
        override fun onClick(view: View) {
            if (!data.isDeleted && isNickname) onProfileClick(data.triggerMemberId)
            else onNotificationClick(data, bindingAdapterPosition)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
            ds.color = Color.parseColor("#FF49494C")
        }
    }

    private fun getSpannablePopularText(name: String, data: NotificationActionModel): SpannableStringBuilder {
        val popularText = name + getPopularContent(data.notificationText)
        val spannablePopularText = SpannableStringBuilder(popularText)
        spannablePopularText.setSpan(
            StyleSpan(com.teamwable.ui.R.font.font_pretendard_semibold),
            0,
            name.replace("\n: ", "").length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannablePopularText
    }

    private fun getSpannableStyle(
        name: String,
        resId: Int,
        endIndex: Int = 0,
        data: NotificationActionModel,
    ): SpannableStringBuilder {
        val spannableText = SpannableStringBuilder(getSpannableText(data, name, resId))

        getClickSpannableStyle(data, spannableText, name, endIndex)
        getBoldSpannableStyle(spannableText, name, endIndex)

        return spannableText
    }

    private fun getClickSpannableStyle(
        data: NotificationActionModel,
        spannableText: SpannableStringBuilder,
        name: String,
        endIndex: Int
    ) {
        when (data.notificationTriggerType) {
            in listOf("contentLiked", "comment", "commentLiked", "popularWriter") -> {
                spannableText.setSpan(
                    clickableSpan(data, true),
                    0,
                    name.length + endIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableText.setSpan(
                    clickableSpan(data, false),
                    data.triggerMemberNickname.length + endIndex + 2,
                    spannableText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tvNotificationVpContent.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun getBoldSpannableStyle(
        spannableText: SpannableStringBuilder,
        name: String,
        endIndex: Int
    ) {
        spannableText.setSpan(
            StyleSpan(com.teamwable.ui.R.font.font_pretendard_semibold),
            0,
            name.length + endIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
    }

    private fun getSpannableText(
        data: NotificationActionModel,
        name: String,
        resId: Int
    ): String {
        val resourceString = if (data.notificationTriggerType in listOf("contentLiked", "commentLiked")) {
            binding.root.context.getString(
                resId,
                dummyUserName
            )
        } else {
            binding.root.context.getString(resId)
        }

        return if (data.notificationTriggerType in listOf("contentLiked", "comment", "commentLiked", "popularWriter", "contentGhost", "commentGhost")) {
            "$name$resourceString\n: ${getPopularContent(data.notificationText)}"
        } else {
            "$name$resourceString"
        }
    }

    private fun getPopularContent(notificationText: String): String {
        return if (notificationText.length > MAX_LEN) {
            notificationText.substring(0, MAX_LEN) + binding.root.context.stringOf(R.string.tv_notification_action_more)
        } else {
            notificationText
        }
    }

    companion object {
        const val MAX_LEN = 15
        const val ACTING_CONTINUE_LEN = 21
    }
}
