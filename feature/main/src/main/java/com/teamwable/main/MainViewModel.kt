package com.teamwable.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _notificationNumberUiState = MutableStateFlow<UiState<Int>>(UiState.Loading)
    val notificationNumberUiState = _notificationNumberUiState.asStateFlow()

    init {
        getNotificationNumber()
    }

    private fun getNotificationNumber() {
        viewModelScope.launch {
            notificationRepository.getNumber()
                .onSuccess { _notificationNumberUiState.value = UiState.Success(it) }
                .onFailure { _notificationNumberUiState.value = UiState.Failure(it.message.toString()) }
        }
    }
}
