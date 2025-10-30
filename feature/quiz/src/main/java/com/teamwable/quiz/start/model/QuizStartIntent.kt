package com.teamwable.quiz.start.model

import com.teamwable.common.base.BaseIntent
import com.teamwable.quiz.component.OXType

sealed interface QuizStartIntent : BaseIntent {
    data object LoadInitialData : QuizStartIntent

    data object ClickAppBarBack : QuizStartIntent

    data object ClickSubmitBtn : QuizStartIntent

    data class ClickOXButton(val type: OXType) : QuizStartIntent
}
