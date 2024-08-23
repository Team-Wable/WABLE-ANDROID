package com.teamwable.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teamwable.data.repository.FeedRepository
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Feed
import com.teamwable.model.Profile
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.model.profile.MemberDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userInfoRepository: UserInfoRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _memberDataUiState = MutableStateFlow<UiState<MemberDataModel>>(UiState.Empty)
    val memberDataUiState = _memberDataUiState.asStateFlow()

    fun updateFeeds(userId: Long): Flow<PagingData<Feed>> = feedRepository.getProfileFeeds(userId)

    fun fetchAuthId(userId: Long) {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { fetchUserType(userId, it) }
        }
    }

    private fun fetchUserType(userId: Long, authId: Long) {
        val userType = if (userId == -1L || userId == authId) {
            ProfileUserType.AUTH
        } else {
            ProfileUserType.MEMBER
        }

        fetchProfileInfo(if (userType == ProfileUserType.AUTH) authId else userId)
        _uiState.value = ProfileUiState.UserTypeDetermined(userType)
    }

    private fun fetchProfileInfo(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileInfo(userId)
                .onSuccess { _uiState.value = ProfileUiState.Success(it) }
                .onFailure { _uiState.value = ProfileUiState.Error(it.message.toString()) }
        }
    }

    fun removeFeed(feedId: Long) {
        viewModelScope.launch {
            feedRepository.deleteFeed(feedId)
                .onSuccess { _uiState.value = ProfileUiState.RemoveFeed(feedId) }
                .onFailure { _uiState.value = ProfileUiState.Error(it.message.toString()) }
        }
    }

    fun getMemberData() {
        viewModelScope.launch {
            _memberDataUiState.value = UiState.Loading
            profileRepository.getMemberData()
                .onSuccess { _memberDataUiState.value = UiState.Success(it) }
                .onFailure { _memberDataUiState.value = UiState.Failure(it.message.toString()) }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Success(val profile: Profile) : ProfileUiState

    data class UserTypeDetermined(val userType: ProfileUserType) : ProfileUiState

    data class RemoveFeed(val feedId: Long) : ProfileUiState

    data class Error(val errorMessage: String) : ProfileUiState
}
