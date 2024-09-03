package com.teamwable.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.CommentRepository
import com.teamwable.model.Comment
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
class ProfileCommentListViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileCommentUiState>(ProfileCommentUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileCommentSideEffect>()
    val event = _event.asSharedFlow()

    private val removedCommentsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedFeedsFlow = MutableStateFlow(setOf<Long>())

    fun updateComments(userId: Long): Flow<PagingData<Comment>> {
        val commentsFlow = commentRepository.getProfileComments(userId).cachedIn(viewModelScope)
        return combine(commentsFlow, removedCommentsFlow, ghostedFeedsFlow) { commentsFlow, removedCommentIds, ghostedUserIds ->
            commentsFlow
                .filter { removedCommentIds.contains(it.commentId).not() }
                .map { data ->
                    if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
                }
        }
    }

    fun removeComment(commentId: Long) {
        viewModelScope.launch {
            commentRepository.deleteComment(commentId)
                .onSuccess {
                    removedCommentsFlow.value = removedCommentsFlow.value.toMutableSet().apply { add(commentId) }
                    _event.emit(ProfileCommentSideEffect.DismissBottomSheet)
                }
                .onFailure { _uiState.value = ProfileCommentUiState.Error(it.message.toString()) }
        }
    }

    fun updateGhost(request: Ghost) {
        viewModelScope.launch {
            commentRepository.postGhost(request)
                .onSuccess {
                    ghostedFeedsFlow.value = ghostedFeedsFlow.value.toMutableSet().apply { add(request.postAuthorId) }
                    _event.emit(ProfileCommentSideEffect.ShowSnackBar)
                }
                .onFailure { _uiState.value = ProfileCommentUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface ProfileCommentUiState {
    data object Loading : ProfileCommentUiState

    data class Success(val profile: Profile) : ProfileCommentUiState

    data class Error(val errorMessage: String) : ProfileCommentUiState
}

sealed interface ProfileCommentSideEffect {
    data object ShowSnackBar : ProfileCommentSideEffect

    data object DismissBottomSheet : ProfileCommentSideEffect
}
