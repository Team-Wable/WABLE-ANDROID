package com.teamwable.ui.type

import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class BottomSheetType(
    @StringRes val title: Int = R.string.label_dialog_blank,
) {
    REPORT(R.string.label_dialog_report_yes),
    DELETE_FEED(R.string.label_dialog_delete_yes),
    EMPTY(),
}
