package com.teamwable.homedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.data.repository.CommentRepository
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Comment
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.model.LikeState
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userInfoRepository: UserInfoRepository,
    private val feedRepository: FeedRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeDetailUiState>(HomeDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeDetailSideEffect>()
    val event = _event.asSharedFlow()

    private val likeFeedsFlow = MutableStateFlow(mapOf<Long, LikeState>())
    private val removedCommentsFlow = MutableStateFlow(setOf<Long>())
    private val ghostedFlow = MutableStateFlow(setOf<Long>())
    private val likeCommentsFlow = MutableStateFlow(mapOf<Long, LikeState>())

    private var authId: Long = -1

    init {
        fetchAuthId()
    }

    private fun fetchAuthId() {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { authId = it }
        }
    }

    fun updateHomeDetailToFlow(feed: Feed): Flow<PagingData<Feed>> {
        val feedFlow = flowOf(PagingData.from(listOf(feed))).cachedIn(viewModelScope)
        return combine(feedFlow, ghostedFlow, likeFeedsFlow) { feedsFlow, ghostedUserIds, likeStates ->
            feedsFlow
                .map { data ->
                    val likeState = likeStates[data.feedId] ?: LikeState(data.isLiked, data.likedNumber)
                    val transformedGhost = if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
                    transformedGhost.copy(likedNumber = likeState.count, isLiked = likeState.isLiked)
                }
        }
    }

    fun updateComments(feedId: Long): Flow<PagingData<Comment>> {
        val commentsFlow = commentRepository.getHomeDetailComments(feedId).cachedIn(viewModelScope)
        return combine(commentsFlow, removedCommentsFlow, ghostedFlow, likeCommentsFlow) { commentsFlow, removedCommentIds, ghostedUserIds, likeStates ->
            commentsFlow
                .filter { removedCommentIds.contains(it.commentId).not() }
                .map { data ->
                    val likeState = likeStates[data.commentId] ?: LikeState(data.isLiked, data.likedNumber)
                    val transformedGhost = if (ghostedUserIds.contains(data.postAuthorId)) data.copy(isPostAuthorGhost = true) else data
                    transformedGhost.copy(likedNumber = likeState.count, isLiked = likeState.isLiked)
                }
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
                .onSuccess {
                    removedCommentsFlow.value = removedCommentsFlow.value.toMutableSet().apply { add(commentId) }
                    _event.emit(HomeDetailSideEffect.DismissBottomSheet)
                }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun addComment(contentId: Long, commentText: String) {
        viewModelScope.launch {
            commentRepository.postComment(contentId, commentText)
                .onSuccess { _event.emit(HomeDetailSideEffect.ShowCommentSnackBar) }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun updateHomeDetailToNetwork(feedId: Long) {
        viewModelScope.launch {
            feedRepository.getHomeDetail(feedId)
                .onSuccess { _uiState.value = HomeDetailUiState.Success(it.copy(feedId = feedId)) }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun removeFeed(feedId: Long) {
        viewModelScope.launch {
            feedRepository.deleteFeed(feedId)
                .onSuccess { _uiState.value = HomeDetailUiState.RemoveFeed(feedId) }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun updateGhost(request: Ghost) {
        viewModelScope.launch {
            commentRepository.postGhost(request)
                .onSuccess {
                    ghostedFlow.value = ghostedFlow.value.toMutableSet().apply { add(request.postAuthorId) }
                    _event.emit(HomeDetailSideEffect.ShowSnackBar(SnackbarType.GHOST))
                }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun reportUser(nickname: String, relateText: String) {
        viewModelScope.launch {
            profileRepository.postReport(nickname, relateText)
                .onSuccess { _event.emit(HomeDetailSideEffect.ShowSnackBar(SnackbarType.REPORT)) }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun updateFeedLike(feedId: Long, likeState: LikeState) {
        val currentLikeState = likeFeedsFlow.value[feedId]
        if (currentLikeState?.isLiked == likeState.isLiked) return

        viewModelScope.launch {
            val result = if (likeState.isLiked) feedRepository.postFeedLike(feedId) else feedRepository.deleteFeedLike(feedId)

            result
                .onSuccess { likeFeedsFlow.update { it.toMutableMap().apply { put(feedId, likeState) } } }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }

    fun updateCommentLike(commentId: Long, commentText: String, likeState: LikeState) {
        val currentLikeState = likeCommentsFlow.value[commentId]
        if (currentLikeState?.isLiked == likeState.isLiked) return

        viewModelScope.launch {
            val result = if (likeState.isLiked) commentRepository.postCommentLike(commentId, commentText) else commentRepository.deleteCommentLike(commentId)

            result
                .onSuccess { likeCommentsFlow.update { it.toMutableMap().apply { put(commentId, likeState) } } }
                .onFailure { _uiState.value = HomeDetailUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState

    data class Success(val feed: Feed) : HomeDetailUiState

    data class RemoveFeed(val feedId: Long) : HomeDetailUiState

    data class Error(val errorMessage: String) : HomeDetailUiState
}

sealed interface HomeDetailSideEffect {
    data class ShowSnackBar(val type: SnackbarType) : HomeDetailSideEffect

    data object ShowCommentSnackBar : HomeDetailSideEffect

    data object DismissBottomSheet : HomeDetailSideEffect
}
