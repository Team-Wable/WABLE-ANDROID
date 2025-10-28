package com.teamwable.quiz.start

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.teamwable.common.base.BaseViewModel
import com.teamwable.data.repository.QuizRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.model.quiz.QuizScoreModel
import com.teamwable.quiz.start.model.QuizStartIntent
import com.teamwable.quiz.start.model.QuizStartSideEffect
import com.teamwable.quiz.start.model.QuizStartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizStartViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<QuizStartIntent, QuizStartState, QuizStartSideEffect>(
        initialState = QuizStartState(),
    ) {
    private var startTimeMillis: Long
        get() = savedStateHandle[KEY_START_TIME] ?: 0L
        set(value) {
            savedStateHandle[KEY_START_TIME] = value
        }

    override fun initialDataLoad() {
        onIntent(QuizStartIntent.LoadInitialData)
    }

    override fun onIntent(intent: QuizStartIntent) {
        when (intent) {
            QuizStartIntent.LoadInitialData -> getQuiz()
            QuizStartIntent.ClickAppBarBack -> postSideEffect(QuizStartSideEffect.NavigateUp)
            QuizStartIntent.ClickSubmitBtn -> submitQuiz()
            is QuizStartIntent.ClickOXButton -> intent { copy(oxType = intent.type) }
        }
    }

    private fun getQuiz() {
        viewModelScope.launch {
            quizRepository.getQuiz()
                .onSuccess { quizModel ->
                    if (startTimeMillis == 0L) startTimeMillis = SystemClock.elapsedRealtime()
                    intent { copy(quizModel = quizModel) }
                }
                .onFailure { postSideEffect(QuizStartSideEffect.ShowSnackBar(it)) }
        }
    }

    private fun submitQuiz() {
        val time = getElapsedTime()
        viewModelScope.launch {
            quizRepository.patchQuizScore(
                QuizScoreModel(
                    quizId = currentState.quizModel.quizId.toLong(),
                    userAnswer = currentState.userAnswer,
                    quizTime = time,
                ),
            )
                .onSuccess { model -> setQuizCompleted(model.copy(time = time)) }
                .onFailure { postSideEffect(QuizStartSideEffect.ShowSnackBar(it)) }
        }
    }

    private fun setQuizCompleted(model: QuizResultModel) {
        viewModelScope.launch {
            userInfoRepository.saveQuizCompleted(true)
            postSideEffect(QuizStartSideEffect.NavigateToResult(model))
        }
    }

    private fun getElapsedTime(): Int {
        val elapsedMillis = SystemClock.elapsedRealtime() - startTimeMillis
        return (elapsedMillis / 1000).toInt()
    }

    companion object {
        private const val KEY_START_TIME = "start_time_millis"
    }
}
