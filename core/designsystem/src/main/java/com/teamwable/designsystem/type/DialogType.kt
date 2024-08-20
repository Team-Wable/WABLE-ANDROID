package com.teamwable.designsystem.type

import androidx.annotation.StringRes
import com.teamwable.designsystem.R

enum class DialogType(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val buttonText: Int,
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
}
