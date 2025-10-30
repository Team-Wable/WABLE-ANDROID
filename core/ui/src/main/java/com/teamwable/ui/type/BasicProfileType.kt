package com.teamwable.ui.type

import androidx.annotation.DrawableRes
import com.teamwable.common.R as r

enum class BasicProfileType(@DrawableRes val profileDrawableRes: Int) {
    PURPLE(r.drawable.ic_share_profile_img_purple),
    BLUE(r.drawable.ic_share_profile_img_blue),
    GREEN(r.drawable.ic_share_profile_img_mint),
}
