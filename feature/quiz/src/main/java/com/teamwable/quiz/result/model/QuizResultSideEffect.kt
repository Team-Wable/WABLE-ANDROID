package com.teamwable.quiz.result.model

import com.teamwable.common.base.SideEffect

sealed interface QuizResultSideEffect : SideEffect {
    data object NavigateToMain : QuizResultSideEffect

    data class ShowSnackBar(val message: Throwable) : QuizResultSideEffect
}
