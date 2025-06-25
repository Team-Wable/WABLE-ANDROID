package com.teamwable.profile.profile.edit.model

import androidx.compose.runtime.Immutable
import com.teamwable.common.base.BaseState
import com.teamwable.common.type.LckTeamType
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class ProfileEditState(
    val nickname: String = "",
    val textFieldType: NicknameType = NicknameType.DEFAULT,
    val selectedImageUri: String? = null,
    val currentImage: ProfileImageType = ProfileImageType.entries.random(),
    val isPermissionGranted: Boolean = false,
    val openDialog: Boolean = false,
    val selectedTeam: LckTeamType = LckTeamType.T1,
    val shuffledTeams: PersistentList<LckTeamType> = LckTeamType.entries.toPersistentList(),
) : BaseState
