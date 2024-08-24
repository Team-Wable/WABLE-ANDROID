package com.teamwable.designsystem.type

import androidx.annotation.DrawableRes

enum class ProfileImageType(
    @DrawableRes val teamProfileImage: Int,
) {
    PURPLE(teamProfileImage = com.teamwable.common.R.drawable.ic_share_profile_img_purple),
    BLUE(teamProfileImage = com.teamwable.common.R.drawable.ic_share_profile_img_blue),
    GREEN(teamProfileImage = com.teamwable.common.R.drawable.ic_share_profile_img_mint),
}
