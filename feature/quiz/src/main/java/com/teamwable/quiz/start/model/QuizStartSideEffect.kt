package com.teamwable.quiz.start.model

import com.teamwable.common.base.SideEffect

sealed interface QuizStartSideEffect : SideEffect {
    data class NavigateToResult(val elapsedTime: Int) : QuizStartSideEffect

    data object NavigateUp : QuizStartSideEffect

    data class ShowSnackBar(val message: Throwable) : QuizStartSideEffect
}
