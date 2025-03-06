package com.teamwable.community.component

import androidx.annotation.StringRes
import com.teamwable.community.R

enum class CommunityButtonType(
    @StringRes val message: Int = R.string.str_community_btn_default,
) {
    DEFAULT(
        message = R.string.str_community_btn_default,
    ),
    FAN_MORE(
        message = R.string.str_community_btn_fan_more,
    ),
    COPY_COMPLETED(
        message = R.string.str_community_btn_copy_completed,
    ),
}
