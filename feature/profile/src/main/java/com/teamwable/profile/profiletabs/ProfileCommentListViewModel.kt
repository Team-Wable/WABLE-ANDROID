package com.teamwable.profile.profiletabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.CommentRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Comment
import com.teamwable.model.Ghost
import com.teamwable.model.LikeState
import com.teamwable.model.Profile
import com.teamwable.ui.type.SnackbarType
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
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileCommentUiState>(ProfileCommentUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileCommentSideEffect>()
    val event = _event.asSharedFlow()

    private val removedCommentsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedCommentsFlow = MutableStateFlow(setOf<Long>())
    private val likeCommentsFlow = MutableStateFlow(mapOf<Long, LikeState>())

    fun updateComments(userId: Long): Flow<PagingData<Comment>> {
        val commentsFlow = commentRepository.getProfileComments(userId).cachedIn(viewModelScope)
        return combine(commentsFlow, removedCommentsFlow, ghostedCommentsFlow, likeCommentsFlow) { commentsFlow, removedCommentIds, ghostedUserIds, likeStates ->
            commentsFlow
                .filter { removedCommentIds.contains(it.commentId).not() }
                .map { data ->
                    val likeState = likeStates[data.commentId] ?: LikeState(data.isLiked, data.likedNumber)
                    val transformedGhost = if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
                    transformedGhost.copy(likedNumber = likeState.count, isLiked = likeState.isLiked)
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
                    ghostedCommentsFlow.value = ghostedCommentsFlow.value.toMutableSet().apply { add(request.postAuthorId) }
                    _event.emit(ProfileCommentSideEffect.ShowSnackBar(SnackbarType.GHOST))
                }
                .onFailure { _uiState.value = ProfileCommentUiState.Error(it.message.toString()) }
        }
    }

    fun reportUser(nickname: String, relateText: String) {
        viewModelScope.launch {
            profileRepository.postReport(nickname, relateText)
                .onSuccess { _event.emit(ProfileCommentSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
                .onFailure { _uiState.value = ProfileCommentUiState.Error(it.message.toString()) }
        }
    }

    fun updateLike(commentId: Long, commentText: String, likeState: LikeState) {
        val currentLikeState = likeCommentsFlow.value[commentId]
        if (currentLikeState?.isLiked == likeState.isLiked) return

        viewModelScope.launch {
            val result = if (likeState.isLiked) commentRepository.postCommentLike(commentId, commentText) else commentRepository.deleteCommentLike(commentId)

            result.onSuccess {
                likeCommentsFlow.value = likeCommentsFlow.value.toMutableMap().apply {
                    put(commentId, likeState)
                }
            }.onFailure { _uiState.value = ProfileCommentUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface ProfileCommentUiState {
    data object Loading : ProfileCommentUiState

    data class Success(val profile: Profile) : ProfileCommentUiState

    data class Error(val errorMessage: String) : ProfileCommentUiState
}

sealed interface ProfileCommentSideEffect {
    data class ShowSnackBar(val type: SnackbarType) : ProfileCommentSideEffect

    data object DismissBottomSheet : ProfileCommentSideEffect
}
