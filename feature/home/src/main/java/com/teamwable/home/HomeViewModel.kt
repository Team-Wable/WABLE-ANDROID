package com.teamwable.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.NotificationRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.home.Feed
import com.teamwable.model.home.Ghost
import com.teamwable.model.home.LikeState
import com.teamwable.model.profile.MemberInfoEditModel
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
class HomeViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userInfoRepository: UserInfoRepository,
    private val profileRepository: ProfileRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeSideEffect>()
    val event = _event.asSharedFlow()

    private val removedFeedsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedFeedsFlow = MutableStateFlow(setOf<Long>())
    private val likeFeedsFlow = MutableStateFlow(mapOf<Long, LikeState>())
    private val banFeedsFlow = MutableStateFlow(setOf<Long>())
    private val feedsFlow = feedRepository.getHomeFeeds().cachedIn(viewModelScope)

    private var authId = -1L
    private var isAdmin = false

    init {
        getNotificationNumber()
        fetchAuthId()
        fetchIsPushAlarmAllowed()
        fetchIsAdmin()
    }

    private fun fetchAuthId() {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest {
                    authId = it
                }
        }
    }

    private fun fetchIsAdmin() = viewModelScope.launch {
        userInfoRepository.getIsAdmin()
            .collectLatest { isAdmin = it }
    }

    private fun fetchIsPushAlarmAllowed() {
        viewModelScope.launch {
            userInfoRepository.getIsPushAlarmAllowed().collectLatest {
                delay(500) // 로딩뷰를 위한 delay
                if (!it) _uiState.value = HomeUiState.AddPushAlarmPermission else _uiState.value = HomeUiState.Success
            }
        }
    }

    fun updateLoadingState() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            delay(500) // 로딩뷰를 위한 delay
            _uiState.value = HomeUiState.Success
        }
    }

    fun updateFeeds(): Flow<PagingData<Feed>> {
        return combine(feedsFlow, removedFeedsFlow, ghostedFeedsFlow, likeFeedsFlow, banFeedsFlow) { feedsFlow, removedFeedIds, ghostedUserIds, likeStates, banState ->
            feedsFlow
                .filter { removedFeedIds.contains(it.feedId).not() }
                .map { data ->
                    val likeState = likeStates[data.feedId] ?: LikeState(data.isLiked, data.likedNumber)
                    val transformedGhost = if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
                    val transformedBan = if (banState.contains(data.feedId)) transformedGhost.copy(isBlind = true) else transformedGhost
                    transformedBan.copy(likedNumber = likeState.count, isLiked = likeState.isLiked)
                }
        }
    }

    fun fetchUserType(userId: Long): ProfileUserType {
        return when {
            userId == authId -> ProfileUserType.AUTH
            authId == -1L -> {
                _uiState.value = HomeUiState.Error("auth id is empty")
                ProfileUserType.EMPTY
            }

            isAdmin -> ProfileUserType.ADMIN

            else -> ProfileUserType.MEMBER
        }
    }

    fun removeFeed(feedId: Long) {
        viewModelScope.launch {
            feedRepository.deleteFeed(feedId)
                .onSuccess {
                    updateFeedRemoveState(feedId)
                    _event.emit(HomeSideEffect.DismissBottomSheet)
                }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }

    fun updateFeedRemoveState(feedId: Long) {
        removedFeedsFlow.update { it.toMutableSet().apply { add(feedId) } }
    }

    fun updateGhost(request: Ghost) {
        viewModelScope.launch {
            feedRepository.postGhost(request)
                .onSuccess {
                    updateFeedGhostState(request.postAuthorId, isGhosted = true)
                    _event.emit(HomeSideEffect.ShowSnackBar(SnackbarType.GHOST))
                }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }

    fun updateFeedGhostState(postAuthorId: Long, isGhosted: Boolean) {
        ghostedFeedsFlow.update { it.toMutableSet().apply { if (isGhosted) add(postAuthorId) } }
    }

    fun reportUser(nickname: String, relateText: String) {
        viewModelScope.launch {
            profileRepository.postReport(nickname, relateText)
                .onSuccess { _event.emit(HomeSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }

    fun updateLike(feedId: Long, likeState: LikeState) {
        val currentLikeState = likeFeedsFlow.value[feedId]
        if (currentLikeState?.isLiked == likeState.isLiked) return

        viewModelScope.launch {
            val result = if (likeState.isLiked) feedRepository.postFeedLike(feedId) else feedRepository.deleteFeedLike(feedId)

            result
                .onSuccess { updateFeedLikeState(feedId, likeState) }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }

    fun updateFeedLikeState(feedId: Long, likeState: LikeState) {
        likeFeedsFlow.update { it.toMutableMap().apply { put(feedId, likeState) } }
    }

    fun patchUserProfileUri(info: MemberInfoEditModel, url: String? = null) {
        viewModelScope.launch {
            profileRepository.patchUserProfile(info, url)
                .onSuccess { saveIsPushAlarmAllowed(info.isPushAlarmAllowed ?: false) }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }

    private fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean) {
        viewModelScope.launch {
            userInfoRepository.saveIsPushAlarmAllowed(isPushAlarmAllowed)
        }
    }

    fun banUser(banInfo: Triple<Long, String, Long>) = viewModelScope.launch {
        profileRepository.postBan(banInfo)
            .onSuccess {
                updateFeedBanState(feedId = banInfo.third, isBan = true)
                _event.emit(HomeSideEffect.ShowSnackBar(SnackbarType.BAN))
            }
            .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
    }

    fun updateFeedBanState(feedId: Long, isBan: Boolean) {
        banFeedsFlow.update { it.toMutableSet().apply { if (isBan) add(feedId) } }
    }

    private fun getNotificationNumber() {
        viewModelScope.launch {
            notificationRepository.getNumber()
                .onSuccess { _uiState.value = HomeUiState.AddNotificationBadge(it) }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object Success : HomeUiState

    data class Error(val errorMessage: String) : HomeUiState

    data object AddPushAlarmPermission : HomeUiState

    data class AddNotificationBadge(val notiCount: Int) : HomeUiState
}

sealed interface HomeSideEffect {
    data class ShowSnackBar(val type: SnackbarType) : HomeSideEffect

    data object DismissBottomSheet : HomeSideEffect
}
