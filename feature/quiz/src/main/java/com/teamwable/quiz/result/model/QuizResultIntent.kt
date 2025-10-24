package com.teamwable.quiz.result.model

import com.teamwable.common.base.BaseIntent
import com.teamwable.model.quiz.QuizResultModel

sealed interface QuizResultIntent : BaseIntent {
    data object ClickXPBtn : QuizResultIntent

    data class LoadInitialData(val model: QuizResultModel) : QuizResultIntent
}
