package com.teamwable.viewit.ui

import com.teamwable.model.viewit.ViewIt

data class ViewItActions(
    val onClickProfile: (Long) -> Unit = {},
    val onClickKebab: (ViewIt) -> Unit = {},
    val onClickLink: (String) -> Unit = {},
    val onClickLike: (ViewIt) -> Unit = {},
    val onClickPosting: () -> Unit = {},
    val onRefresh: () -> Unit = {},
)
