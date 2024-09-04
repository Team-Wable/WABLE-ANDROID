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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    init {
        binding.root.setOnClickListener {
            if (this::item.isInitialized) onNotificationClick(item, adapterPosition)
        }

        binding.ivNotificationVpProfile.setOnClickListener {
            if (this::item.isInitialized) onProfileClick(item.triggerMemberId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NotificationActionModel) {
        item = data

        with(binding) {

            val spannableText = when (data.notificationTriggerType) {
                root.context.stringOf(NotificationActionType.CONTENT_LIKED.title) ->
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        NotificationActionType.CONTENT_LIKED.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.COMMENT.title) ->
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        NotificationActionType.COMMENT.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.COMMENT_LIKED.title) ->
                    getSpannableStyle(
                        data.triggerMemberNickname,
                        NotificationActionType.COMMENT_LIKED.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.ACTING_CONTINUE.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.ACTING_CONTINUE.content,
                        ACTING_CONTINUE_LEN,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.BE_GHOST.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.BE_GHOST.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.CONTENT_GHOST.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.CONTENT_GHOST.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.COMMENT_GHOST.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.COMMENT_GHOST.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.USER_BAN.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.USER_BAN.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.POPULAR_WRITER.title) ->
                    getSpannableStyle(
                        data.memberNickname,
                        NotificationActionType.POPULAR_WRITER.content,
                        data = data
                    )

                root.context.stringOf(NotificationActionType.POPULAR_CONTENT.title) ->
                    getSpannablePopularText(
                        root.context.getString(NotificationActionType.POPULAR_CONTENT.content),
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
        if (data.notificationTriggerType != binding.root.context.stringOf(NotificationActionType.POPULAR_CONTENT.title)) {
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
        val resourceString = if (data.notificationTriggerType in listOf(
                binding.root.context.stringOf(NotificationActionType.CONTENT_LIKED.title),
                binding.root.context.stringOf(NotificationActionType.COMMENT_LIKED.title)
            )
        ) {
            binding.root.context.getString(
                resId,
                data.memberNickname
            )
        } else {
            binding.root.context.getString(resId)
        }

        return if (data.notificationTriggerType in listOf(
                binding.root.context.stringOf(NotificationActionType.CONTENT_LIKED.title),
                binding.root.context.stringOf(NotificationActionType.COMMENT.title),
                binding.root.context.stringOf(NotificationActionType.COMMENT_LIKED.title),
                binding.root.context.stringOf(NotificationActionType.POPULAR_WRITER.title)
            )
        ) {
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

        fun from(
            parent: ViewGroup,
            onNotificationClick: (NotificationActionModel, Int) -> Unit,
            onProfileClick: (Int) -> Unit
        ): NotificationActionViewHolder =
            NotificationActionViewHolder(
                ItemNotificationVpBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onNotificationClick,
                onProfileClick
            )
    }
}
