package com.teamwable.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UI_INTENT : BaseIntent, UI_STATE : BaseState, SIDE_EFFECT : SideEffect>(
    initialState: UI_STATE,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UI_STATE> by lazy {
        _uiState
            .onStart { initialDataLoad() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = initialState,
            )
    }

    private val _sideEffect: Channel<SIDE_EFFECT> = Channel(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    protected val currentState: UI_STATE
        get() = _uiState.value

    open fun initialDataLoad() {}

    abstract fun onIntent(intent: UI_INTENT) // handle event from ui

    protected fun intent(reduce: UI_STATE.() -> UI_STATE) { // set state
        _uiState.update { currentState.reduce() }
    }

    protected fun postSideEffect(vararg effects: SIDE_EFFECT) { // post side effect
        viewModelScope.launch {
            effects.forEach { effect ->
                _sideEffect.send(effect)
            }
        }
    }
}
