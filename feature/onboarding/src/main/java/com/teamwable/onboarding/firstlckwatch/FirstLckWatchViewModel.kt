package com.teamwable.onboarding.firstlckwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.onboarding.firstlckwatch.model.FirstLckWatchSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstLckWatchViewModel @Inject constructor() : ViewModel() {
    private val _firstLckWatchSideEffect: MutableSharedFlow<FirstLckWatchSideEffect> = MutableSharedFlow()
    val firstLckWatchSideEffect: SharedFlow<FirstLckWatchSideEffect>
        get() = _firstLckWatchSideEffect.asSharedFlow()

    fun navigateToSelectTeam() {
        viewModelScope.launch {
            _firstLckWatchSideEffect.emit(FirstLckWatchSideEffect.NavigateToSelectLckTeam)
        }
    }
}
