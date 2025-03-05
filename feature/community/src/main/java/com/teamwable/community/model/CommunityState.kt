package com.teamwable.community.model

import com.teamwable.common.base.BaseState
import com.teamwable.common.type.LckTeamType
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.designsystem.type.DialogType

data class CommunityState(
    val progress: Float = 0f,
    val lckTeams: List<LckTeamType> = LckTeamType.entries,
    val selectedTeamName: String? = null,
    val preRegisterTeamName: String = "",
    val buttonState: CommunityButtonType = CommunityButtonType.DEFAULT,
    val dialogType: DialogType? = null,
    val isSelected: Boolean = false,
    val isPushPermission: Boolean = false,
) : BaseState
