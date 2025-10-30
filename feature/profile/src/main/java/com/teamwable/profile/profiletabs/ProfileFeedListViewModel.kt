package com.teamwable.profile.profiletabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import com.teamwable.model.home.Feed
import com.teamwable.model.home.Ghost
import com.teamwable.model.home.LikeState
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFeedListViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileFeedUiState>(ProfileFeedUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileFeedSideEffect>()
    val event = _event.asSharedFlow()

    private val removedFeedsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedFeedsFlow = MutableStateFlow(setOf<Long>())
    private val likeFeedsFlow = MutableStateFlow(mapOf<Long, LikeState>())
    private val banFeedsFlow = MutableStateFlow(setOf<Long>())

    fun updateFeeds(userId: Long): Flow<PagingData<Feed>> {
        val feedsFlow = feedRepository.getProfileFeeds(userId).cachedIn(viewModelScope)
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

    fun removeFeed(feedId: Long) {
        viewModelScope.launch {
            feedRepository.deleteFeed(feedId)
                .onSuccess {
                    removedFeedsFlow.value = removedFeedsFlow.value.toMutableSet().apply { add(feedId) }
                    _event.emit(ProfileFeedSideEffect.DismissBottomSheet)
                }
                .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
        }
    }

    fun updateGhost(request: Ghost) {
        viewModelScope.launch {
            feedRepository.postGhost(request)
                .onSuccess {
                    ghostedFeedsFlow.value = ghostedFeedsFlow.value.toMutableSet().apply { add(request.postAuthorId) }
                    _event.emit(ProfileFeedSideEffect.ShowSnackBar(SnackbarType.GHOST))
                }
                .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
        }
    }

    fun reportUser(nickname: String, relateText: String) {
        viewModelScope.launch {
            profileRepository.postReport(nickname, relateText)
                .onSuccess { _event.emit(ProfileFeedSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
                .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
        }
    }

    fun updateLike(feedId: Long, likeState: LikeState) {
        val currentLikeState = likeFeedsFlow.value[feedId]
        if (currentLikeState?.isLiked == likeState.isLiked) return

        viewModelScope.launch {
            val result = if (likeState.isLiked) feedRepository.postFeedLike(feedId) else feedRepository.deleteFeedLike(feedId)

            result
                .onSuccess { likeFeedsFlow.update { it.toMutableMap().apply { put(feedId, likeState) } } }
                .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
        }
    }

    fun banUser(banInfo: Triple<Long, String, Long>) = viewModelScope.launch {
        profileRepository.postBan(banInfo)
            .onSuccess {
                banFeedsFlow.value = banFeedsFlow.value.toMutableSet().apply { add(banInfo.third) }
                _event.emit(ProfileFeedSideEffect.ShowSnackBar(SnackbarType.BAN))
            }
            .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
    }
}

sealed interface ProfileFeedUiState {
    data object Loading : ProfileFeedUiState

    data class Success(val profile: Profile) : ProfileFeedUiState

    data class Error(val errorMessage: String) : ProfileFeedUiState
}

sealed interface ProfileFeedSideEffect {
    data class ShowSnackBar(val type: SnackbarType) : ProfileFeedSideEffect

    data object DismissBottomSheet : ProfileFeedSideEffect
}
