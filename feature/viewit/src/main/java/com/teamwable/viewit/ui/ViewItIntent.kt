package com.teamwable.viewit.ui

import com.teamwable.common.base.BaseIntent
import com.teamwable.model.viewit.ViewIt

sealed interface ViewItIntent : BaseIntent {
    data class ClickProfile(val viewIt: ViewIt) : ViewItIntent

    data class ClickKebabBtn(val viewIt: ViewIt) : ViewItIntent

    data class ClickLikeBtn(val viewIt: ViewIt) : ViewItIntent

    data class ClickLink(val viewIt: ViewIt) : ViewItIntent
}
