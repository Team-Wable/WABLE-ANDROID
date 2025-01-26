package com.teamwable.onboarding.profile.model

import com.teamwable.common.base.SideEffect
import com.teamwable.model.profile.MemberInfoEditModel

sealed interface ProfileSideEffect : SideEffect {
    data object RequestImagePicker : ProfileSideEffect

    data class NavigateToAgreeTerms(val memberInfoEditModel: MemberInfoEditModel) : ProfileSideEffect

    data class ShowSnackBar(val message: Throwable) : ProfileSideEffect

    data object NavigateToProfile : ProfileSideEffect
}
