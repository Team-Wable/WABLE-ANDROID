package com.teamwable.ui.type

import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class AlarmTriggerType(@StringRes val type: Int) {
    COMMENT(R.string.label_ghost_comment_type),
    CONTENT(R.string.label_ghost_content_type),
}
