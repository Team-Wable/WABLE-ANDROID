package com.teamwable.profile.profiletabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.FeedRepository
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFeedListViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileFeedUiState>(ProfileFeedUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileFeedSideEffect>()
    val event = _event.asSharedFlow()

    private val removedFeedsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedFeedsFlow = MutableStateFlow(setOf<Long>())

    fun updateFeeds(userId: Long): Flow<PagingData<Feed>> {
        val feedsFlow = feedRepository.getProfileFeeds(userId).cachedIn(viewModelScope)
        return combine(feedsFlow, removedFeedsFlow, ghostedFeedsFlow) { feedsFlow, removedFeedIds, ghostedUserIds ->
            feedsFlow
                .filter { removedFeedIds.contains(it.feedId).not() }
                .map { data ->
                    if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
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
                    _event.emit(ProfileFeedSideEffect.ShowSnackBar)
                }
                .onFailure { _uiState.value = ProfileFeedUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface ProfileFeedUiState {
    data object Loading : ProfileFeedUiState

    data class Success(val profile: Profile) : ProfileFeedUiState

    data class Error(val errorMessage: String) : ProfileFeedUiState
}

sealed interface ProfileFeedSideEffect {
    data object ShowSnackBar : ProfileFeedSideEffect

    data object DismissBottomSheet : ProfileFeedSideEffect
}
