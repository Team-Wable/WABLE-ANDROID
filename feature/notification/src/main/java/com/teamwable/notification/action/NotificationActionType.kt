package com.teamwable.notification.action

import androidx.annotation.StringRes
import com.teamwable.notification.R

enum class NotificationActionType(@StringRes val title: Int, @StringRes val content: Int) {
    CONTENT_LIKED(title = R.string.label_notification_action_content_liked, content = R.string.tv_notification_action_content_liked),
    COMMENT(title = R.string.label_notification_action_feed_comment, content = R.string.tv_notification_action_feed_comment),
    COMMENT_LIKED(title = R.string.label_notification_action_comment_liked, content = R.string.tv_notification_action_comment_liked),
    ACTING_CONTINUE(title = R.string.label_notification_action_acting_continue, content = R.string.tv_notification_action_acting_continue),
    BE_GHOST(title = R.string.label_notification_action_be_ghost, content = R.string.tv_notification_action_be_ghost),
    CONTENT_GHOST(title = R.string.label_notification_action_content_ghost, content = R.string.tv_notification_action_content_ghost),
    COMMENT_GHOST(title = R.string.label_notification_action_comment_ghost, content = R.string.tv_notification_action_comment_ghost),
    USER_BAN(title = R.string.label_notification_action_user_ban, content = R.string.tv_notification_action_user_ban),
    POPULAR_WRITER(title = R.string.label_notification_action_popular_writer, content = R.string.tv_notification_action_popular_writer),
    POPULAR_CONTENT(title = R.string.label_notification_action_popular_content, content = R.string.tv_notification_action_popular_content),
    CHILD_COMMENT(title = R.string.label_notification_action_feed_child_comment, content = R.string.tv_notification_action_feed_child_comment),
    CHILD_COMMENT_LIKED(title = R.string.label_notification_action_child_comment_liked, content = R.string.tv_notification_action_chile_comment_liked),
    VIEW_IT_LIKED(title = R.string.label_notification_action_view_it_liked, content = R.string.tv_notification_action_view_it_liked),
}
