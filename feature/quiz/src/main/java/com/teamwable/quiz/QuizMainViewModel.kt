package com.teamwable.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.domain.usecase.GetRemainingTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuizMainViewModel @Inject constructor(
    userInfoRepository: UserInfoRepository,
    getRemainingTimeUseCase: GetRemainingTimeUseCase,
) : ViewModel() {
    val isQuizCompleted: StateFlow<Boolean?> = userInfoRepository.getQuizCompleted()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val remainingTime: StateFlow<String> = getRemainingTimeUseCase.invoke()
        .map { (hours, minutes) -> "%d:%02d".format(hours, minutes) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "00:00",
        )
}
