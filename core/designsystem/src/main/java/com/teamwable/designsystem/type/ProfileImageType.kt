package com.teamwable.designsystem.type

import androidx.annotation.DrawableRes

enum class ProfileImageType(
    @DrawableRes val image: Int,
) {
    PURPLE(image = com.teamwable.common.R.drawable.ic_share_profile_img_purple),
    BLUE(image = com.teamwable.common.R.drawable.ic_share_profile_img_blue),
    GREEN(image = com.teamwable.common.R.drawable.ic_share_profile_img_mint),
}
