package com.teamwable.ui.type

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.teamwable.ui.R

enum class DialogType(
    @StringRes val title: Int = R.string.label_dialog_blank,
    @StringRes val description: Int = R.string.label_dialog_blank,
    @StringRes val noLabel: Int = R.string.label_dialog_share_no,
    @StringRes val yesLabel: Int = R.string.label_dialog_delete_yes,
    @StyleRes val titleTypo: Int = R.style.TextAppearance_Wable_Head2,
) {
    REPORT(
        title = R.string.label_dialog_report_title,
        description = R.string.label_dialog_report_description,
        yesLabel = R.string.label_dialog_report_yes,
        titleTypo = R.style.TextAppearance_Wable_Head1,
    ),
    DELETE_FEED(
        title = R.string.label_dialog_delete_feed_title,
        description = R.string.label_dialog_delete_feed_description,
        titleTypo = R.style.TextAppearance_Wable_Head1,
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
        titleTypo = R.style.TextAppearance_Wable_Body1,
    ),
    EMPTY(),
}
