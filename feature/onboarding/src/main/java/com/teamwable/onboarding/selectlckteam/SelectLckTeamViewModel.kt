package com.teamwable.onboarding.selectlckteam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.onboarding.selectlckteam.model.SelectLckTeamSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectLckTeamViewModel @Inject constructor() : ViewModel() {
    private val _firstLckWatchSideEffect: MutableSharedFlow<SelectLckTeamSideEffect> = MutableSharedFlow()
    val firstLckWatchSideEffect: SharedFlow<SelectLckTeamSideEffect>
        get() = _firstLckWatchSideEffect.asSharedFlow()

    fun navigateToProfile() {
        viewModelScope.launch {
            _firstLckWatchSideEffect.emit(SelectLckTeamSideEffect.NavigateToProfile)
        }
    }
}
