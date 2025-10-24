package com.teamwable.quiz.result

import androidx.lifecycle.viewModelScope
import com.teamwable.common.base.BaseViewModel
import com.teamwable.common.base.EmptyState
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.quiz.result.model.QuizResultIntent
import com.teamwable.quiz.result.model.QuizResultSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : BaseViewModel<QuizResultIntent, EmptyState, QuizResultSideEffect>(
        initialState = EmptyState,
    ) {
    override fun onIntent(intent: QuizResultIntent) {
        when (intent) {
            QuizResultIntent.ClickBottomBtn -> setQuizCompleted()
            QuizResultIntent.LoadInitialData -> {}
        }
    }

    private fun setQuizCompleted() {
        viewModelScope.launch {
            userInfoRepository.saveQuizCompleted(true)
            postSideEffect(QuizResultSideEffect.NavigateToMain)
        }
    }
}
