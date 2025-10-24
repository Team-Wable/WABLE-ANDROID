package com.teamwable.quiz.result.model

import com.teamwable.common.base.BaseIntent

sealed interface QuizResultIntent : BaseIntent {
    data object LoadInitialData : QuizResultIntent

    data object ClickBottomBtn : QuizResultIntent
}
