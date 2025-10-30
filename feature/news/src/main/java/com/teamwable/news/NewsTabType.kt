package com.teamwable.news

import androidx.annotation.StringRes

enum class NewsTabType(@StringRes val title: Int = R.string.tv_news_tab_news) {
    MATCH,
    RANK,
    CURATION(title = R.string.tv_news_tab_curation),
    NOTICE(title = R.string.tv_news_tab_notice),
}
