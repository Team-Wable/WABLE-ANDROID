package com.teamwable.community.model

import androidx.compose.runtime.Stable
import com.teamwable.common.base.BaseState
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.designsystem.type.DialogType
import com.teamwable.model.community.CommunityModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class CommunityState(
    val progress: Float = 0f,
    val lckTeams: PersistentList<CommunityModel> = persistentListOf(),
    val selectedTeamName: String = "",
    val preRegisterTeamName: String = "",
    val buttonState: CommunityButtonType = CommunityButtonType.DEFAULT,
    val dialogType: DialogType? = null,
    val isSelected: Boolean = false,
    val isPushPermission: Boolean = false,
) : BaseState
