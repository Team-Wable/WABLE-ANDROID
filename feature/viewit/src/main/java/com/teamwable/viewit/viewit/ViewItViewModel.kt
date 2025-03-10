package com.teamwable.viewit.viewit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.home.LikeState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewItViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val viewItRepository: ViewItRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewItUiState>(ViewItUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ViewItSideEffect>()
    val event = _event.asSharedFlow()

    private val authId = MutableStateFlow(-1L)
    private val isAdmin = MutableStateFlow(false)

    private val viewItsFlow = viewItRepository.getViewIts().cachedIn(viewModelScope)
    private val removedViewItsFlow = MutableStateFlow(setOf<Long>())

    init {
        fetchAuthId()
        fetchIsAdmin()
    }

    private fun fetchAuthId() {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { id ->
                    authId.update { id }
                    if (id == -1L) _uiState.value = ViewItUiState.Error("auth id is empty")
                }
        }
    }

    private fun fetchIsAdmin() = viewModelScope.launch {
        userInfoRepository.getIsAdmin()
            .collectLatest { isAdmin.update { it } }
    }

    fun fetchUserType(userId: Long): ProfileUserType {
        return when {
            userId == authId.value -> ProfileUserType.AUTH
            isAdmin.value -> ProfileUserType.ADMIN
            else -> ProfileUserType.MEMBER
        }
    }

    fun updateViewIts(): Flow<PagingData<ViewIt>> {
        return combine(viewItsFlow, removedViewItsFlow) { viewItsFlow, removedViewItIds ->
            viewItsFlow
                .filter { removedViewItIds.contains(it.viewItId).not() }
        }
    }

    fun updateLike(viewItId: Long, likeState: LikeState) = viewModelScope.launch {
        val result = if (likeState.isLiked) viewItRepository.postViewItLike(viewItId) else viewItRepository.deleteViewItLike(viewItId)
        result.onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
    }

    fun removeViewIt(viewId: Long) = viewModelScope.launch {
        viewItRepository.deleteViewIt(viewId)
            .onSuccess {
                updateViewItRemoveState(viewId)
                _event.emit(ViewItSideEffect.DismissBottomSheet)
            }
            .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
    }

    private fun updateViewItRemoveState(viewItId: Long) {
        removedViewItsFlow.update { it.toMutableSet().apply { add(viewItId) } }
    }

    fun reportUser(nickname: String, relateText: String) {
        viewModelScope.launch {
            profileRepository.postReport(nickname, relateText)
                .onSuccess { _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
                .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
        }
    }

    fun banUser(banInfo: Triple<Long, String, Long>) = viewModelScope.launch {
        profileRepository.postBan(banInfo)
            .onSuccess { _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.BAN)) }
            .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
    }

    fun postViewIt(link: String, content: String) = viewModelScope.launch {
        _uiState.value = ViewItUiState.Loading
        viewItRepository.postViewIt(link, content)
            .onSuccess {
                _uiState.value = ViewItUiState.Success
                delay(200)
                _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.VIEW_IT_COMPLETE))
            }
            .onFailure {
                _event.emit(
                    ViewItSideEffect.ShowErrorMessage(it),
                )
            }
    }
}

sealed interface ViewItUiState {
    data object Loading : ViewItUiState

    data object Success : ViewItUiState

    data class Error(val errorMessage: String) : ViewItUiState
}

sealed interface ViewItSideEffect {
    data class ShowSnackBar(val type: SnackbarType) : ViewItSideEffect

    data object DismissBottomSheet : ViewItSideEffect

    data class ShowErrorMessage(val throwable: Throwable) : ViewItSideEffect
}
