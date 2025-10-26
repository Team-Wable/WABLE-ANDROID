package com.teamwable.quiz.result.model

import androidx.compose.runtime.Stable
import com.teamwable.common.base.BaseState
import com.teamwable.quiz.component.QuizResultType

@Stable
data class QuizResultState(
    val scoreTime: String = "0:00",
    val quizResult: Boolean = false,
    val userPercent: Int = 0,
) : BaseState {
    val resultType: QuizResultType
        get() = if (quizResult) QuizResultType.SUCCESS else QuizResultType.FAIL
    val xp: Int
        get() = if (quizResult) 8 else 3
}
