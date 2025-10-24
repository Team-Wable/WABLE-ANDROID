package com.teamwable.quiz.start

import androidx.lifecycle.viewModelScope
import com.teamwable.common.base.BaseViewModel
import com.teamwable.data.repository.QuizRepository
import com.teamwable.data.repository.UserInfoRepository
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
) : BaseViewModel<QuizStartIntent, QuizStartState, QuizStartSideEffect>(
        initialState = QuizStartState(),
    ) {
    private var startTimeMillis: Long = 0L

    override fun initialDataLoad() {
        startTimeMillis = System.currentTimeMillis()
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
                .onSuccess { quizModel -> intent { copy(quizModel = quizModel) } }
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
                .onSuccess { setQuizCompleted(time) }
                .onFailure { postSideEffect(QuizStartSideEffect.ShowSnackBar(it)) }
        }
    }

    private fun setQuizCompleted(time: Int) {
        viewModelScope.launch {
            userInfoRepository.saveQuizCompleted(true)
            postSideEffect(QuizStartSideEffect.NavigateToResult(time))
        }
    }

    private fun getElapsedTime(): Int {
        val endTimeMillis = System.currentTimeMillis()
        val elapsedSeconds = (endTimeMillis - startTimeMillis) / 1000
        return elapsedSeconds.toInt()
    }
}
