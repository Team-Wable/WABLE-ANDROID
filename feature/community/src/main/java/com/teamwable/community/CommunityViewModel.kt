package com.teamwable.community

import androidx.lifecycle.viewModelScope
import com.teamwable.common.base.BaseViewModel
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.community.model.CommunityIntent
import com.teamwable.community.model.CommunitySideEffect
import com.teamwable.community.model.CommunityState
import com.teamwable.community.model.toLckTeamType
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.designsystem.type.DialogType
import com.teamwable.domain.usecase.GetJoinedCommunityNameUseCase
import com.teamwable.domain.usecase.GetSortedCommunityListUseCase
import com.teamwable.model.community.CommunityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val getSortedCommunityListUseCase: GetSortedCommunityListUseCase,
    private val getJoinedCommunityNameUseCase: GetJoinedCommunityNameUseCase,
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
            is CommunityIntent.UpdatePhotoPermission -> intent { copy(isPushPermission = intent.isGranted) }
        }
    }

    private fun getInitialData() {
        viewModelScope.launch {
            getJoinedCommunity()
            getSortedCommunityList()
        }
    }

    private suspend fun getJoinedCommunity() {
        getJoinedCommunityNameUseCase()
            .catch {
                postSideEffect(CommunitySideEffect.ShowSnackBar(it))
            }
            .collectLatest { joinedCommunity ->
                intent { copy(preRegisterTeamName = joinedCommunity) }
            }
    }

    private suspend fun getSortedCommunityList() {
        getSortedCommunityListUseCase(currentState.preRegisterTeamName)
            .catch {
                postSideEffect(CommunitySideEffect.ShowSnackBar(it))
            }
            .collectLatest { (communityList, progress) ->
                val mappedLckTeams = communityList.mapNotNull(CommunityModel::toLckTeamType)
                intent {
                    copy(
                        lckTeams = mappedLckTeams.toPersistentList(),
                        progress = progress,
                        buttonState = if (preRegisterTeamName.isNotEmpty()) CommunityButtonType.FAN_MORE else CommunityButtonType.DEFAULT,
                    )
                }
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
                    showPushNotificationDialog()
                    getSortedCommunityList()
                }.onFailure {
                    dismissDialog()
                    postSideEffect(CommunitySideEffect.ShowSnackBar(it))
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
        viewModelScope.launch {
            delay(3000L)
            dismissDialog()
            if (!currentState.isPushPermission) {
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
            intent { copy(buttonState = CommunityButtonType.FAN_MORE) }
        }
    }

    private fun copyToClipBoard() {
        postSideEffect(CommunitySideEffect.CopyToClipBoard)
    }

    private fun navigateToPushAlarm() {
        postSideEffect(CommunitySideEffect.NavigateToPushAlarm)
        dismissDialog()
    }

    private fun dismissDialog() {
        intent { copy(dialogType = null) }
    }
}
