package com.teamwable.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teamwable.data.repository.CommentRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Comment
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
class HomeDetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeDetailUiState>(HomeDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var authId: Long = -1

    init {
        fetchAuthId()
    }

    fun updateComments(feedId: Long): Flow<PagingData<Comment>> = commentRepository.getHomeDetailComments(feedId)

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
                _uiState.value = HomeDetailUiState.Error("auth id is empty")
                ProfileUserType.EMPTY
            }

            else -> ProfileUserType.MEMBER
        }
    }

    fun removeComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(commentId)
                .onSuccess { _uiState.value = HomeDetailUiState.RemoveComment(commentId) }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState

    data class Success(val profile: Profile) : HomeDetailUiState

    data class RemoveComment(val commentId: Long) : HomeDetailUiState

    data class Error(val errorMessage: String) : HomeDetailUiState
}
