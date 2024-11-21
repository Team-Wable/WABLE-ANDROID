package com.teamwable.ui.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class SnackbarType(
    @StringRes val message: Int = R.string.label_dialog_blank,
    @DrawableRes val icon: Int = com.teamwable.common.R.drawable.ic_home_toast_success,
) {
    // TODO : 프로그래스바 로티
    GHOST(R.string.label_snack_bar_ghost),
    COMMENT_ING(R.string.label_snack_bar_comment_ing),
    COMMENT_COMPLETE(R.string.label_snack_bar_comment_complete),
    REPORT(R.string.label_snack_bar_report),
    BAN(R.string.label_snack_bar_ban),
}
