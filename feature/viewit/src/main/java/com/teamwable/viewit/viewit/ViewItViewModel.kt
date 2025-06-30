package com.teamwable.viewit.viewit

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.common.base.BaseViewModel
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.domain.usecase.GetAuthTypeUseCase
import com.teamwable.model.home.LikeState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.extensions.addItem
import com.teamwable.ui.extensions.putItem
import com.teamwable.ui.type.BanTriggerType
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.viewit.ui.ViewItIntent
import com.teamwable.viewit.ui.ViewItSideEffect
import com.teamwable.viewit.ui.ViewItUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewItViewModel @Inject constructor(
    private val viewItRepository: ViewItRepository,
    private val profileRepository: ProfileRepository,
    private val getAuthTypeUseCase: GetAuthTypeUseCase,
) : BaseViewModel<ViewItIntent, ViewItUiState, ViewItSideEffect>(
        initialState = ViewItUiState(),
    ) {
    private val removedViewItsFlow = MutableStateFlow(setOf<Long>())
    private val banViewItFlow = MutableStateFlow(setOf<Long>())
    private val likeViewItFlow = MutableStateFlow(mapOf<Long, LikeState>())
    val viewItPagingFlow: Flow<PagingData<ViewIt>> = combine(
        viewItRepository.getViewIts().cachedIn(viewModelScope),
        removedViewItsFlow,
        banViewItFlow,
        likeViewItFlow,
    ) { pagingData, removed, banned, likeStates ->
        pagingData
            .filter { it.viewItId !in removed }
            .map { viewIt ->
                val isBanned = banned.contains(viewIt.viewItId)
                val likeState = likeStates[viewIt.viewItId] ?: LikeState(viewIt.isLiked, viewIt.likedNumber)

                viewIt.copy(
                    isBlind = isBanned,
                    isLiked = likeState.isLiked,
                    likedNumber = likeState.count,
                )
            }
    }

    override fun onIntent(intent: ViewItIntent) {
        when (intent) {
            is ViewItIntent.ClickKebabBtn -> onKebabBtnClick(intent.viewIt)
            is ViewItIntent.ClickLikeBtn -> onLikeClick(intent.viewIt)
            is ViewItIntent.ClickLink -> onLinkClick(intent.url)
            is ViewItIntent.ClickProfile -> onProfileClick(intent.id)
            is ViewItIntent.BanViewIt -> onBanUser()
            is ViewItIntent.RemoveViewIt -> onRemoveViewIt()
            is ViewItIntent.ReportViewIt -> onReportViewIt()
            is ViewItIntent.PostViewIt -> onPostViewIt(intent.link, intent.content)
            ViewItIntent.ClickPosting -> onPostingClick()
            ViewItIntent.PullToRefresh -> onRefreshViewIt()
        }
    }

    private fun onRefreshViewIt() {
        postSideEffect(ViewItSideEffect.UI.Refresh)
    }

    private fun onPostingClick() {
        postSideEffect(ViewItSideEffect.Navigation.ToPosting)
    }

    private fun onProfileClick(id: Long) = viewModelScope.launch {
        when (getAuthTypeUseCase.invoke(id)) {
            ProfileUserType.AUTH.name -> postSideEffect(ViewItSideEffect.Navigation.ToMyProfile)
            ProfileUserType.MEMBER.name -> postSideEffect(ViewItSideEffect.Navigation.ToMemberProfile(id))
            else -> Unit
        }
    }

    private fun onKebabBtnClick(viewIt: ViewIt) = viewModelScope.launch {
        val type = when (getAuthTypeUseCase.invoke(viewIt.postAuthorId)) {
            ProfileUserType.AUTH.name -> BottomSheetType.DELETE_FEED
            ProfileUserType.ADMIN.name -> BottomSheetType.BAN
            else -> BottomSheetType.REPORT
        }
        intent {
            copy(pendingViewIt = viewIt, isBottomSheetVisible = true, bottomSheetType = type)
        }
        postSideEffect(ViewItSideEffect.UI.ShowBottomSheet(type, viewIt))
    }

    private fun onLinkClick(url: String) {
        postSideEffect(ViewItSideEffect.Navigation.ToUrl(url))
    }

    private fun onRemoveViewIt() = viewModelScope.launch {
        val id = currentState.pendingViewIt?.viewItId ?: return@launch

        dismissBottomSheet()
        viewItRepository.deleteViewIt(id)
            .onSuccess { removedViewItsFlow.addItem(id) }
            .onFailure { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR, it)) }
    }

    private fun onReportViewIt() = viewModelScope.launch {
        val viewit = currentState.pendingViewIt ?: return@launch

        dismissBottomSheet()
        profileRepository.postReport(viewit.postAuthorNickname, viewit.viewItContent)
            .onSuccess { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.REPORT)) }
            .onFailure { postSideEffect(ViewItSideEffect.Navigation.ToError) }
    }

    private fun onBanUser() = viewModelScope.launch {
        val viewit = currentState.pendingViewIt ?: return@launch

        dismissBottomSheet()
        profileRepository.postBan(Triple(viewit.postAuthorId, BanTriggerType.CONTENT.name.lowercase(), viewit.viewItId))
            .onSuccess {
                banViewItFlow.addItem(viewit.viewItId)
                postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.BAN))
            }
            .onFailure { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR, it)) }
    }

    private fun dismissBottomSheet() {
        intent { copy(pendingViewIt = null, isBottomSheetVisible = false, bottomSheetType = BottomSheetType.EMPTY) }
        postSideEffect(ViewItSideEffect.UI.DismissBottomSheet)
    }

    private fun onPostViewIt(link: String, content: String) = viewModelScope.launch {
        postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.VIEW_IT_ING))
        viewItRepository.postViewIt(link, content)
            .onSuccess {
                postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.VIEW_IT_COMPLETE))
                onRefreshViewIt()
            }
            .onFailure { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR, it)) }
    }

    private fun onLikeClick(viewIt: ViewIt) {
        val id = viewIt.viewItId
        val likeState = LikeState(viewIt.isLiked, viewIt.likedNumber)

        likeViewItFlow.putItem(id, likeState.toggle())

        viewModelScope.launch {
            val result = if (likeState.toggle().isLiked) viewItRepository.postViewItLike(id) else viewItRepository.deleteViewItLike(id)
            result.onFailure {
                likeViewItFlow.putItem(id, likeState)
                postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR, it))
            }
        }
    }
}
