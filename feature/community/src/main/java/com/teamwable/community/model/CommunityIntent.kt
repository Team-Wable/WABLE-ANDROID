package com.teamwable.community.model

import com.teamwable.common.base.BaseIntent

sealed interface CommunityIntent : BaseIntent {
    data object LoadInitialData : CommunityIntent

    data object ClickFloatingBtn : CommunityIntent

    data object ClickPreRegisterBtn : CommunityIntent

    data object ClickPushBtn : CommunityIntent

    data object ClickDismissBtn : CommunityIntent

    data object ClickPreRegisterDismissBtn : CommunityIntent

    data class ClickDefaultItemBtn(val selectTeamName: String) : CommunityIntent

    data object ClickMoreFanItemBtn : CommunityIntent

    data object OpenPushNotiDialog : CommunityIntent

    data class UpdatePhotoPermission(val isGranted: Boolean) : CommunityIntent
}
