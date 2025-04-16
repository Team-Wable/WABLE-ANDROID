package com.teamwable.viewit.viewit

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.common.base.BaseViewModel
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.home.LikeState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.viewit.ui.ViewItIntent
import com.teamwable.viewit.ui.ViewItSideEffect
import com.teamwable.viewit.ui.ViewItUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ViewItViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val viewItRepository: ViewItRepository,
    private val profileRepository: ProfileRepository,
) : BaseViewModel<ViewItIntent, ViewItUiState, ViewItSideEffect>(
        initialState = ViewItUiState(),
    ) {
    private val removedViewItsFlow = MutableStateFlow(setOf<Long>())
    private val banViewItFlow = MutableStateFlow(setOf<Long>())
    private val likeViewItFlow = MutableStateFlow(mapOf<Long, LikeState>())
    val viewItPagingFlow: Flow<PagingData<ViewIt>> = viewItRepository.getViewIts()
        .combine(removedViewItsFlow) { pagingData, removed ->
            pagingData.filter { it.viewItId !in removed }
        }
        .combine(banViewItFlow) { pagingData, banned ->
            pagingData.map { viewIt ->
                if (banned.contains(viewIt.viewItId)) {
                    viewIt.copy(isBlind = true)
                } else {
                    viewIt
                }
            }
        }
        .combine(likeViewItFlow) { pagingData, likeStates ->
            pagingData.map { viewIt ->
                val like = likeStates[viewIt.viewItId] ?: LikeState(viewIt.isLiked, viewIt.likedNumber)
                viewIt.copy(isLiked = like.isLiked, likedNumber = like.count)
            }
        }
        .cachedIn(viewModelScope)

    override fun onIntent(intent: ViewItIntent) {
        when (intent) {
            is ViewItIntent.ClickKebabBtn -> onKebabBtnClick(intent.viewIt)
            is ViewItIntent.ClickLikeBtn -> TODO()
            is ViewItIntent.ClickLink -> TODO()
            is ViewItIntent.ClickProfile -> TODO()
        }
    }

    private fun onKebabBtnClick(viewIt: ViewIt) {
    }

    private fun onLikeBtnClick(viewIt: ViewIt) {
    }

    private fun onProfileClick(viewIt: ViewIt) {
    }

    private fun onLinkClick(viewIt: ViewIt) {
    }
}

//
//    private val _uiState = MutableStateFlow<ViewItUiState>(ViewItUiState.Loading)
//    val uiState = _uiState.asStateFlow()
//
//
//    private val _event = MutableSharedFlow<ViewItSideEffect>()
//    val event = _event.asSharedFlow()

private val authId = MutableStateFlow(-1L)
private val isAdmin = MutableStateFlow(false)

//    init {
//        fetchAuthId()
//        fetchIsAdmin()
//    }

//    private fun fetchAuthId() {
//        viewModelScope.launch {
//            userInfoRepository.getMemberId()
//                .map { it.toLong() }
//                .collectLatest { id ->
//                    authId.update { id }
//                    if (id == -1L) _uiState.value = ViewItUiState.Error("auth id is empty")
//                }
//        }
//    }
//
//    private fun fetchIsAdmin() = viewModelScope.launch {
//        userInfoRepository.getIsAdmin()
//            .collectLatest { value -> isAdmin.update { value } }
//    }
//
//    fun fetchUserType(userId: Long): ProfileUserType {
//        return when {
//            userId == authId.value -> ProfileUserType.AUTH
//            isAdmin.value -> ProfileUserType.ADMIN
//            else -> ProfileUserType.MEMBER
//        }
//    }
//

//
//    fun updateLike(viewItId: Long, likeState: LikeState) = viewModelScope.launch {
//        val result = if (likeState.isLiked) viewItRepository.postViewItLike(viewItId) else viewItRepository.deleteViewItLike(viewItId)
//        result
//            .onSuccess { updateViewItLikeState(viewItId, likeState) }
//            .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
//    }
//
//    private fun updateViewItLikeState(feedId: Long, likeState: LikeState) {
//        likeViewItFlow.update { it.toMutableMap().apply { put(feedId, likeState) } }
//    }
//
//    fun removeViewIt(viewId: Long) = viewModelScope.launch {
//        viewItRepository.deleteViewIt(viewId)
//            .onSuccess {
//                updateViewItRemoveState(viewId)
//                _event.emit(ViewItSideEffect.DismissBottomSheet)
//            }
//            .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
//    }
//
//    private fun updateViewItRemoveState(viewItId: Long) {
//        removedViewItsFlow.update { it.toMutableSet().apply { add(viewItId) } }
//    }
//
//    fun reportUser(nickname: String, relateText: String) {
//        viewModelScope.launch {
//            profileRepository.postReport(nickname, relateText)
//                .onSuccess { _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
//                .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
//        }
//    }
//
//    fun banUser(banInfo: Triple<Long, String, Long>) = viewModelScope.launch {
//        profileRepository.postBan(banInfo)
//            .onSuccess {
//                updateViewItBanState(viewItId = banInfo.third, isBan = true)
//                _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.BAN))
//            }
//            .onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
//    }
//
//    private fun updateViewItBanState(viewItId: Long, isBan: Boolean) {
//        banViewItFlow.update { it.toMutableSet().apply { if (isBan) add(viewItId) } }
//    }
//
//    fun postViewIt(link: String, content: String) = viewModelScope.launch {
//        _uiState.value = ViewItUiState.Loading
//        viewItRepository.postViewIt(link, content)
//            .onSuccess {
//                _uiState.value = ViewItUiState.Success
//                delay(200)
//                _event.emit(ViewItSideEffect.ShowSnackBar(SnackbarType.VIEW_IT_COMPLETE))
//            }
//            .onFailure {
//                _event.emit(
//                    ViewItSideEffect.ShowErrorMessage(it),
//                )
//            }
//    }
// }

// sealed interface ViewItUiState {
//    data object Loading : ViewItUiState
//
//    data object Success : ViewItUiState
//
//    data class Error(val errorMessage: String) : ViewItUiState
// }

// sealed interface ViewItSideEffect {
//    data class ShowSnackBar(val type: SnackbarType) : ViewItSideEffect
//
//    data object DismissBottomSheet : ViewItSideEffect
//
//    data class ShowErrorMessage(val throwable: Throwable) : ViewItSideEffect
// }
