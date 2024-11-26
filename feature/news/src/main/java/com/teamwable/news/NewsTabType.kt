package com.teamwable.news

import androidx.annotation.StringRes

enum class NewsTabType(@StringRes val title: Int = R.string.tv_news_tab_news) {
    MATCH,
    RANK,
    NEWS(title = R.string.tv_news_tab_news),
    NOTICE(title = R.string.tv_news_tab_notice),
}
