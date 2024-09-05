package com.teamwable.onboarding.selectlckteam.model

sealed interface SelectLckTeamSideEffect {
    data object NavigateToProfile : SelectLckTeamSideEffect

    data class ShowSnackBar(val message: String) : SelectLckTeamSideEffect
}
