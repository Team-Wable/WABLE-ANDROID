package com.teamwable.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Feed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun updateFeeds(userId: Long): Flow<PagingData<Feed>> = feedRepository.getProfileFeeds(userId).map {
        Timber.e(it.toString())
        it
    }

    fun fetchUserId(memberId: Long?) {
        viewModelScope.launch {
            userInfoRepository.getMemberId().collectLatest { _uiState.value = ProfileUiState.FetchUserId(it) }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    class FetchUserId(val userId: Int) : ProfileUiState
}
