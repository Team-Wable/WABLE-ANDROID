package com.teamwable.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Feed
import com.teamwable.model.Profile
import com.teamwable.ui.type.ProfileUserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var authId: Long = -1

    init {
        fetchAuthId()
    }

    fun updateFeeds(): Flow<PagingData<Feed>> = feedRepository.getHomeFeeds()

    private fun fetchAuthId() {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { authId = it }
        }
    }

    fun fetchUserType(userId: Long): ProfileUserType {
        return when {
            userId == authId -> ProfileUserType.AUTH
            authId == -1L -> {
                _uiState.value = HomeUiState.Error("auth id is empty")
                ProfileUserType.EMPTY
            }

            else -> ProfileUserType.MEMBER
        }
    }

    fun removeFeed(feedId: Long) {
        viewModelScope.launch {
            feedRepository.deleteFeed(feedId)
                .onSuccess { _uiState.value = HomeUiState.RemoveFeed(feedId) }
                .onFailure { _uiState.value = HomeUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(val profile: Profile) : HomeUiState

    data class RemoveFeed(val feedId: Long) : HomeUiState

    data class Error(val errorMessage: String) : HomeUiState
}
