package com.teamwable.viewit.ui

import com.teamwable.common.base.BaseState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.type.BottomSheetType

data class ViewItActions(
    val onClickProfile: (Long) -> Unit = {},
    val onClickKebab: (ViewIt) -> Unit = {},
    val onClickLink: (String) -> Unit = {},
    val onClickLike: (ViewIt) -> Unit = {},
    val onClickPosting: () -> Unit = {},
    val onRefresh: () -> Unit = {},
)

data class ViewItUiState(
    val pendingViewIt: ViewIt? = null,
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetType: BottomSheetType = BottomSheetType.EMPTY,
) : BaseState
