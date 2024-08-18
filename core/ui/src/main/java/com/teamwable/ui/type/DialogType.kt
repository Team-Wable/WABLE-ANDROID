package com.teamwable.ui.type

import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class DialogType(
    @StringRes val title: Int = R.string.label_dialog_blank,
    @StringRes val description: Int = R.string.label_dialog_blank,
    @StringRes val noLabel: Int = R.string.label_dialog_share_no,
    @StringRes val yesLabel: Int = R.string.label_dialog_delete_yes,
) {
    REPORT(
        title = R.string.label_dialog_report_title,
        description = R.string.label_dialog_report_description,
        yesLabel = R.string.label_dialog_report_yes,
    ),
    DELETE_FEED(
        title = R.string.label_dialog_delete_feed_title,
        description = R.string.label_dialog_delete_feed_description,
    ),
    DELETE_ACCOUNT(
        title = R.string.label_dialog_delete_account_title,
    ),
    LOGOUT(
        title = R.string.label_dialog_logout_title,
        yesLabel = R.string.label_dialog_logout_yes,
    ),
    TRANSPARENCY(
        title = R.string.label_dialog_transparency_title,
        noLabel = R.string.label_dialog_transparency_no,
        yesLabel = R.string.label_dialog_transparency_yes,
    ),
    CANCEL_POSTING(
        title = R.string.label_dialog_cancel_posting_title,
        yesLabel = R.string.label_dialog_cancel_posting__yes,
    ),
    EMPTY(),
}
