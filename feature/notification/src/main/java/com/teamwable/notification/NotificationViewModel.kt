package com.teamwable.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NotificationRepository
import com.teamwable.model.NotificationActionModel
import com.teamwable.model.NotificationInformationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel() {
    val mockNotificationEmptyList = emptyList<Any>()

    val mockNotificationActionList = listOf(
        NotificationActionModel(0, "배차은우", "배차은우", "", "contentLiked", "2024-08-18 00:00:00", 0, "안녕? 난 차은우보다 잘생긴 배 차은우 하하", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "comment", "2024-08-17 06:50:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "commentLiked", "2024-08-17 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "actingContinue", "2024-08-16 20:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "beGhost", "2024-08-16 18:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "contentGhost", "2024-08-14 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "commentGhost", "2024-08-13 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "userBan", "2024-08-13 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "popularWriter", "2024-08-12 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
    )

    private val _checkUiState = MutableSharedFlow<UiState<Boolean>>()
    val checkUiState = _checkUiState.asSharedFlow()

    private val _informationUiState = MutableStateFlow<UiState<List<NotificationInformationModel>>>(UiState.Empty)
    val informationUiState = _informationUiState.asStateFlow()

    fun patchCheck() =
        viewModelScope.launch {
            _checkUiState.emit(UiState.Loading)
            notificationRepository.patchCheck()
                .onSuccess { _checkUiState.emit(UiState.Success(it)) }
                .onFailure { _checkUiState.emit(UiState.Failure(it.message.toString())) }
        }

    fun getInformation() {
        viewModelScope.launch {
            _informationUiState.value = UiState.Loading
            notificationRepository.getInformation()
                .onSuccess { _informationUiState.value = UiState.Success(it) }
                .onFailure { _informationUiState.value = UiState.Failure(it.message.toString()) }
        }
    }
}
