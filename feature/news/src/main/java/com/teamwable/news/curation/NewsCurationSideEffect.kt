package com.teamwable.news.curation

import com.teamwable.common.base.SideEffect

sealed interface NewsCurationSideEffect : SideEffect {
    sealed interface Navigation : NewsCurationSideEffect {
        data class ToUrl(val url: String) : Navigation
    }

    sealed interface UI : NewsCurationSideEffect {
        data object Refresh : UI
    }
}
