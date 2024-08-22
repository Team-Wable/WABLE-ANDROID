package com.teamwable.onboarding.selectlckteam.model

sealed interface SelectLckTeamSideEffect {
    data object NavigateToSelectLckTeam : SelectLckTeamSideEffect

    data class ShowSnackBar(val message: String) : SelectLckTeamSideEffect
}
