package com.teamwable.designsystem.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.designsystem.R

enum class SnackBarType(
    @DrawableRes val image: Int = com.teamwable.common.R.drawable.ic_home_toast_success,
    @StringRes val message: Int = R.string.empty,
) {
    WARNING(image = com.teamwable.common.R.drawable.ic_home_toast_warning),
    ERROR(image = com.teamwable.common.R.drawable.ic_home_toast_error),
    SUCCESS(image = com.teamwable.common.R.drawable.ic_home_toast_success),
    LOADING(message = R.string.snackbar_text_loading),
}
