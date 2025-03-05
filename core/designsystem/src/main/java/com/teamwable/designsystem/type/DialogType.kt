package com.teamwable.designsystem.type

import androidx.annotation.StringRes
import com.teamwable.designsystem.R

enum class DialogType(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val buttonText: Int = R.string.empty,
    @StringRes val cancelButtonText: Int = R.string.dialog_pre_register_cancel_btn_text,
) {
    LOGIN(
        title = R.string.dialog_login_title,
        description = R.string.dialog_login_description,
        buttonText = R.string.dialog_login_btn_text,
    ),
    WELLCOME(
        title = R.string.dialog_wellcome_title,
        description = R.string.dialog_wellcome_description,
        buttonText = R.string.dialog_wellcome_btn_text,
    ),
    RRE_REGISTER(
        title = R.string.dialog_pre_register_title,
        description = R.string.dialog_pre_register_description,
        buttonText = R.string.dialog_pre_register_btn_text,
        cancelButtonText = R.string.dialog_pre_register_cancel_btn_text,
    ),
    REGISTER_COMPLETED(
        title = R.string.dialog_register_completed_title,
        description = R.string.dialog_register_completed_description,
    ),
    PUSH_NOTIFICATION(
        title = R.string.dialog_push_notification_title,
        description = R.string.dialog_push_notification_description,
        buttonText = R.string.dialog_push_notification_btn_text,
        cancelButtonText = R.string.dialog_push_notification_cancel_btn_text,
    ),
}
