package com.teamwable.quiz.start.model

import androidx.compose.runtime.Stable
import com.teamwable.common.base.BaseState
import com.teamwable.model.quiz.QuizModel
import com.teamwable.quiz.component.OXType

@Stable
data class QuizStartState(
    val quizModel: QuizModel = QuizModel(),
    val oxType: OXType? = null,
) : BaseState {
    val enabled: Boolean
        get() = oxType != null
    val userAnswer: Boolean
        get() = oxType == OXType.O
}
