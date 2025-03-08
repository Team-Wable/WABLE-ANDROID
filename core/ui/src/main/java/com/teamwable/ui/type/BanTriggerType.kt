package com.teamwable.ui.type

import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class BanTriggerType(
    @StringRes val type: Int,
) {
    COMMENT(type = R.string.label_ban_trigger_type_comment),
    CONTENT(type = R.string.label_ban_trigger_type_content),
    VIEWIT(type = R.string.label_ban_trigger_type_viewit),
}
