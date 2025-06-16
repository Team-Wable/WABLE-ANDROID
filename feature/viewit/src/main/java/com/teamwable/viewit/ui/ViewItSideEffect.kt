package com.teamwable.viewit.ui

import com.teamwable.common.base.SideEffect
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.SnackbarType

sealed interface ViewItSideEffect : SideEffect {
    sealed interface Navigation : ViewItSideEffect {
        data object ToMyProfile : Navigation

        data class ToMemberProfile(val id: Long) : Navigation

        data object ToError : Navigation

        data object ToPosting : Navigation

        data class ToUrl(val url: String) : Navigation
    }

    sealed interface UI : ViewItSideEffect {
        data class ShowSnackBar(val type: SnackbarType, val throwable: Throwable? = null) : UI

        data class ShowBottomSheet(val type: BottomSheetType, val info: Any) : UI

        data object DismissBottomSheet : UI

        data object Refresh : UI
    }
}
