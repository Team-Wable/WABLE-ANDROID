package com.teamwable.news

import androidx.annotation.StringRes

enum class NewsTabType(
    val idx: Int,
    @StringRes val title: Int = R.string.tv_news_tab_news,
) {
    MATCH(0),
    RANK(1),
    NEWS(2, title = R.string.tv_news_tab_news),
    NOTICE(3, title = R.string.tv_news_tab_notice),
}
