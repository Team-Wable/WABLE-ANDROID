package com.teamwable.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamwable.data.repository.CommentRepository
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Comment
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
class ProfileViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userInfoRepository: UserInfoRepository,
    private val profileRepository: ProfileRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var cachedFeeds: Flow<PagingData<Feed>>? = null
    private var cachedComments: Flow<PagingData<Comment>>? = null
    var cachedProfile: Profile? = null

    private val _currentUserId = MutableStateFlow<Long?>(null)

    fun updateFeeds(userId: Long): Flow<PagingData<Feed>> {
        return cachedFeeds ?: feedRepository.getProfileFeeds(userId)
            .cachedIn(viewModelScope)
            .also { cachedFeeds = it }
    }

    fun updateComments(userId: Long): Flow<PagingData<Comment>> {
        return cachedComments ?: commentRepository.getProfileComments(userId)
            .cachedIn(viewModelScope)
            .also { cachedComments = it }
    }

    fun fetchAuthId(userId: Long) {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { fetchUserType(userId, it) }
        }
    }

    private fun fetchUserType(userId: Long, authId: Long) {
        val userType = if (userId == -1L || userId == authId) ProfileUserType.AUTH else ProfileUserType.MEMBER
        _uiState.value = ProfileUiState.UserTypeDetermined(userType)
        if (_currentUserId.value != userId) {
            _currentUserId.value = userId
            fetchProfileInfo(if (userType == ProfileUserType.AUTH) authId else userId)
        } else {
            cachedProfile?.let { _uiState.value = ProfileUiState.Success(it) }
        }
    }

    private fun fetchProfileInfo(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileInfo(userId)
                .onSuccess {
                    cachedProfile = it
                    _uiState.value = ProfileUiState.Success(it)
                }
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

    fun removeComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(commentId)
                .onSuccess { _uiState.value = ProfileUiState.RemoveComment(commentId) }
                .onFailure { _uiState.value = ProfileUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Success(val profile: Profile) : ProfileUiState

    data class UserTypeDetermined(val userType: ProfileUserType) : ProfileUiState

    data class RemoveFeed(val feedId: Long) : ProfileUiState

    data class RemoveComment(val commentId: Long) : ProfileUiState

    data class Error(val errorMessage: String) : ProfileUiState
}
