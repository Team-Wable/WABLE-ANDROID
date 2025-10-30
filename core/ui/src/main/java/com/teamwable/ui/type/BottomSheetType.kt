package com.teamwable.ui.type

import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class BottomSheetType(
    @StringRes val title: Int = R.string.label_dialog_blank,
) {
    REPORT(R.string.label_dialog_report_yes),
    DELETE_FEED(R.string.label_dialog_delete_yes),
    BAN(R.string.label_dialog_ban_yes),
    EMPTY(),
}

fun BottomSheetType.toDialogType(): DialogType = when (this) {
    BottomSheetType.DELETE_FEED -> DialogType.DELETE_FEED
    BottomSheetType.REPORT -> DialogType.REPORT
    BottomSheetType.BAN -> DialogType.BAN
    BottomSheetType.EMPTY -> DialogType.EMPTY
}
