package com.teamwable.onboarding.agreeterms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.onboarding.agreeterms.model.AgreeTermsSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreeTermsViewModel @Inject constructor() : ViewModel() {
    private val _sideEffect = MutableSharedFlow<AgreeTermsSideEffect>()
    val sideEffect: SharedFlow<AgreeTermsSideEffect> = _sideEffect.asSharedFlow()

    fun navigateToHome() {
        viewModelScope.launch {
            _sideEffect.emit(AgreeTermsSideEffect.NavigateToHome)
        }
    }
}
