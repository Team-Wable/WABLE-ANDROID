package com.teamwable.designsystem.type

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.teamwable.designsystem.R
import com.teamwable.designsystem.theme.Error
import com.teamwable.designsystem.theme.Gray600
import com.teamwable.designsystem.theme.Success

enum class NicknameType(
    val color: Color = Gray600,
    @StringRes val message: Int = R.string.profile_edit_nickname_text,
) {
    DEFAULT(
        color = Gray600,
        message = R.string.profile_edit_nickname_text,
    ),
    CORRECT(
        color = Success,
        message = R.string.profile_edit_nickname_success,
    ),
    DUPLICATE(
        color = Error,
        message = R.string.profile_edit_nickname_duplicate,
    ),
    INVALID(
        color = Error,
        message = R.string.profile_edit_nickname_false,
    ),
}
