package com.teamwable.quiz.result

import com.teamwable.common.base.BaseViewModel
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.quiz.result.model.QuizResultIntent
import com.teamwable.quiz.result.model.QuizResultSideEffect
import com.teamwable.quiz.result.model.QuizResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor() : BaseViewModel<QuizResultIntent, QuizResultState, QuizResultSideEffect>(
    initialState = QuizResultState(),
) {
    override fun onIntent(intent: QuizResultIntent) {
        when (intent) {
            QuizResultIntent.ClickXPBtn -> postSideEffect(QuizResultSideEffect.NavigateToMain)
            is QuizResultIntent.LoadInitialData -> updateInitState(intent.model)
        }
    }

    private fun updateInitState(model: QuizResultModel) {
        intent {
            copy(
                scoreTime = formatTime(model.time),
                quizResult = model.quizResult,
                userPercent = model.userPercent,
            )
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.KOREA, "%d:%02d", minutes, remainingSeconds)
    }
}
