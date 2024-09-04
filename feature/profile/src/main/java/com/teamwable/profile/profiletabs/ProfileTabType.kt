package com.teamwable.profile.profiletabs

import androidx.annotation.StringRes
import com.teamwable.profile.R

enum class ProfileTabType(@StringRes val label: Int) {
    FEED(R.string.label_profile_tab_feed),
    COMMENT(R.string.label_profile_tab_comment),
}
