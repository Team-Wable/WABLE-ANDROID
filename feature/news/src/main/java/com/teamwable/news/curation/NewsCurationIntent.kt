package com.teamwable.news.curation

import com.teamwable.common.base.BaseIntent

sealed interface NewsCurationIntent : BaseIntent {
    data class ClickLink(val url: String) : NewsCurationIntent

    data object PullToRefresh : NewsCurationIntent
}
