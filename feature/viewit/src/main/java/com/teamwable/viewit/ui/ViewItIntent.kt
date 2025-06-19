package com.teamwable.viewit.ui

import com.teamwable.common.base.BaseIntent
import com.teamwable.model.viewit.ViewIt

sealed interface ViewItIntent : BaseIntent {
    data class ClickProfile(val id: Long) : ViewItIntent

    data class ClickKebabBtn(val viewIt: ViewIt) : ViewItIntent

    data class ClickLikeBtn(val viewIt: ViewIt) : ViewItIntent

    data class ClickLink(val url: String) : ViewItIntent

    data object ClickPosting : ViewItIntent

    data object RemoveViewIt : ViewItIntent

    data object ReportViewIt : ViewItIntent

    data object BanViewIt : ViewItIntent

    data class PostViewIt(val link: String, val content: String) : ViewItIntent

    data object PullToRefresh : ViewItIntent
}
