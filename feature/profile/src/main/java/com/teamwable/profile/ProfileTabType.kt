package com.teamwable.profile

import androidx.annotation.StringRes

enum class ProfileTabType(@StringRes val label: Int) {
    FEED(R.string.label_profile_tab_feed),
    COMMENT(R.string.label_profile_tab_comment),
}
