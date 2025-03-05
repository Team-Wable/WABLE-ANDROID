package com.teamwable.community

import androidx.lifecycle.viewModelScope
import com.teamwable.common.base.BaseViewModel
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.community.model.CommunityIntent
import com.teamwable.community.model.CommunitySideEffect
import com.teamwable.community.model.CommunityState
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.designsystem.type.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
) : BaseViewModel<CommunityIntent, CommunityState, CommunitySideEffect>(
        initialState = CommunityState(),
    ) {
    init {
        onIntent(CommunityIntent.LoadInitialData)
    }

    override fun onIntent(intent: CommunityIntent) {
        when (intent) {
            is CommunityIntent.LoadInitialData -> getInitialData()
            is CommunityIntent.ClickDefaultItemBtn -> showPreRegisterDialog(intent.selectTeamName)
            CommunityIntent.ClickDismissBtn -> dismissDialog()
            CommunityIntent.ClickPreRegisterBtn -> patchPreInCommunity()
            CommunityIntent.ClickPreRegisterDismissBtn -> resetPreRegister()
            CommunityIntent.ClickPushBtn -> navigateToPushAlarm()
            CommunityIntent.ClickFloatingBtn -> postSideEffect(CommunitySideEffect.NavigateToGoogleForm)
            CommunityIntent.ClickMoreFanItemBtn -> showCopyCompletedDialog()
            CommunityIntent.OpenPushNotiDialog -> showPushNotificationDialog()
            is CommunityIntent.UpdatePhotoPermission -> intent { copy(isPushPermission = intent.isGranted) }
        }
    }

    private fun getInitialData() {
        viewModelScope.launch {
            getJoinedCommunity()
            getCommunityList()
        }
    }

    private suspend fun getJoinedCommunity() {
        communityRepository.getJoinedCommunity()
            .onSuccess { joinedCommunity ->
                intent { copy(preRegisterTeamName = joinedCommunity) }
            }.onFailure {
                postSideEffect(CommunitySideEffect.ShowSnackBar(it.message.orEmpty()))
            }
    }

    private suspend fun getCommunityList() {
        communityRepository.getCommunityInfo()
            .onSuccess { communityList ->
                intent {
                    copy(
                        progress = communityList.find { it.communityName == preRegisterTeamName }?.communityNum ?: 0f,
                        buttonState = if (preRegisterTeamName.isNotEmpty()) CommunityButtonType.FAN_MORE else CommunityButtonType.DEFAULT,
                    )
                }
            }.onFailure {
                postSideEffect(CommunitySideEffect.ShowSnackBar(it.message.orEmpty()))
            }
    }

    private fun patchPreInCommunity() {
        if (currentState.selectedTeamName.isNullOrBlank()) return
        viewModelScope.launch {
            communityRepository.patchPreinCommunity(currentState.selectedTeamName.orEmpty())
                .onSuccess {
                    intent {
                        copy(
                            preRegisterTeamName = currentState.selectedTeamName.orEmpty(),
                            buttonState = CommunityButtonType.FAN_MORE,
                            dialogType = DialogType.PRE_REGISTER_COMPLETED,
                        )
                    }
                    getCommunityList()
                }.onFailure {
                    dismissDialog()
                    postSideEffect(CommunitySideEffect.ShowSnackBar(it.message.orEmpty()))
                }
        }
    }

    private fun resetPreRegister() {
        dismissDialog()
        intent { copy(selectedTeamName = null) }
    }

    private fun showPreRegisterDialog(name: String) {
        intent {
            copy(
                selectedTeamName = name,
                dialogType = DialogType.PRE_REGISTER,
            )
        }
    }

    private fun showPushNotificationDialog() {
        if (!currentState.isPushPermission) {
            viewModelScope.launch {
                delay(3000L)
                intent { copy(dialogType = DialogType.PUSH_NOTIFICATION) }
            }
        }
    }

    private fun showCopyCompletedDialog() {
        intent {
            copy(
                dialogType = DialogType.COPY_COMPLETED,
                buttonState = CommunityButtonType.COPY_COMPLETED,
            )
        }
        copyToClipBoard()
        viewModelScope.launch {
            delay(3000L)
            dismissDialog()
        }
    }

    private fun copyToClipBoard() {
        postSideEffect(CommunitySideEffect.CopyToClipBoard("https://www.naver.com"))
    }

    private fun navigateToPushAlarm() {
        postSideEffect(CommunitySideEffect.NavigateToPushAlarm)
        dismissDialog()
    }

    private fun dismissDialog() {
        intent { copy(dialogType = null) }
    }
}
