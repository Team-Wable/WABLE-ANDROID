package com.teamwable.viewit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.home.LikeState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.type.ProfileUserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewItViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val viewItRepository: ViewItRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewItUiState>(ViewItUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val authId = MutableStateFlow(-1L)
    private val isAdmin = MutableStateFlow(false)

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

    fun updateViewIts(): Flow<PagingData<ViewIt>> = viewItRepository.getViewIts().cachedIn(viewModelScope)

    fun updateLike(viewItId: Long, likeState: LikeState) = viewModelScope.launch {
        val result = if (likeState.isLiked) viewItRepository.postViewItLike(viewItId) else viewItRepository.deleteViewItLike(viewItId)
        result.onFailure { _uiState.value = ViewItUiState.Error(it.message.toString()) }
    }
}

sealed interface ViewItUiState {
    data object Loading : ViewItUiState

    data object Success : ViewItUiState

    data class Error(val errorMessage: String) : ViewItUiState
}
