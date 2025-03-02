package com.teamwable.ui.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.ui.R

enum class AppBarType(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val isBeta: Boolean,
) {
    NEWS(
        title = R.string.label_app_bar_news,
        icon = com.teamwable.common.R.drawable.ic_news_appbar,
        isBeta = false,
    ),
    VIEW_IT(
        title = R.string.label_app_bar_view_it,
        icon = com.teamwable.common.R.drawable.ic_news_appbar,
        isBeta = false,
    ),
    COMMUNITY(
        title = R.string.label_app_bar_community,
        icon = com.teamwable.common.R.drawable.ic_community_appbar,
        isBeta = true,
    ),
}
