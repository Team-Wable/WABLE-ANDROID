package com.teamwable.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NotificationRepository
import com.teamwable.model.notification.NotificationActionModel
import com.teamwable.model.notification.NotificationInformationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _checkUiState = MutableSharedFlow<UiState<Boolean>>()
    val checkUiState = _checkUiState.asSharedFlow()

    fun patchCheck() =
        viewModelScope.launch {
            _checkUiState.emit(UiState.Loading)
            notificationRepository.patchCheck()
                .onSuccess { _checkUiState.emit(UiState.Success(it)) }
                .onFailure { _checkUiState.emit(UiState.Failure(it.message.toString())) }
        }

    fun getNotifications(): Flow<PagingData<NotificationActionModel>> = notificationRepository.getNotifications()

    fun getInformation(): Flow<PagingData<NotificationInformationModel>> = notificationRepository.getInformation()
}
