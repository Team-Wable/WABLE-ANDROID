package com.teamwable.quiz.start.model

import com.teamwable.common.base.SideEffect
import com.teamwable.model.quiz.QuizResultModel

sealed interface QuizStartSideEffect : SideEffect {
    data class NavigateToResult(val model: QuizResultModel) : QuizStartSideEffect

    data object NavigateUp : QuizStartSideEffect

    data class ShowSnackBar(val message: Throwable) : QuizStartSideEffect
}
