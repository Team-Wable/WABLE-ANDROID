package com.teamwable.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.DummyRepository
import com.teamwable.model.Dummy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DummyViewModel @Inject constructor(
    private val dummyRepository: DummyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<DummyUiState>(DummyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadDummy() {
        viewModelScope.launch {
            dummyRepository.getDummy()
                .onSuccess {
                    _uiState.value = DummyUiState.LoadSuccess(it)
                }.onFailure {
                    _uiState.value = DummyUiState.Error(it)
                }
        }
    }
}

sealed interface DummyUiState {
    data object Loading : DummyUiState
    data class LoadSuccess(val dummy: List<Dummy>) : DummyUiState
    data class Error(val exception: Throwable) : DummyUiState
}