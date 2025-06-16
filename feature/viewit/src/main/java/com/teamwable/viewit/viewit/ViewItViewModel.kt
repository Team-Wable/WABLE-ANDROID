package com.teamwable.viewit.viewit

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.teamwable.common.base.BaseViewModel
import com.teamwable.common.base.EmptyState
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.domain.usecase.GetAuthTypeUseCase
import com.teamwable.model.home.LikeState
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.extensions.addItem
import com.teamwable.ui.extensions.putItem
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.viewit.ui.ViewItIntent
import com.teamwable.viewit.ui.ViewItSideEffect
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
) : BaseViewModel<ViewItIntent, EmptyState, ViewItSideEffect>(
        initialState = EmptyState,
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
            is ViewItIntent.BanViewIt -> onBanUser(intent.banInfo)
            is ViewItIntent.RemoveViewIt -> onRemoveViewIt(intent.id)
            is ViewItIntent.ReportViewIt -> onReportViewIt(intent.nickname, intent.relateText)
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
        postSideEffect(ViewItSideEffect.UI.ShowBottomSheet(type, viewIt))
    }

    private fun onLinkClick(url: String) {
        postSideEffect(ViewItSideEffect.Navigation.ToUrl(url))
    }

    private fun onRemoveViewIt(id: Long) = viewModelScope.launch {
        postSideEffect(ViewItSideEffect.UI.DismissBottomSheet)
        viewItRepository.deleteViewIt(id)
            .onSuccess { removedViewItsFlow.addItem(id) }
            .onFailure { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR)) }
    }

    private fun onReportViewIt(nickname: String, relateText: String) = viewModelScope.launch {
        postSideEffect(ViewItSideEffect.UI.DismissBottomSheet)
        profileRepository.postReport(nickname, relateText)
            .onSuccess { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.REPORT)) }
            .onFailure { postSideEffect(ViewItSideEffect.Navigation.ToError) }
    }

    private fun onBanUser(banInfo: Triple<Long, String, Long>) = viewModelScope.launch {
        postSideEffect(ViewItSideEffect.UI.DismissBottomSheet)
        profileRepository.postBan(banInfo)
            .onSuccess {
                banViewItFlow.addItem(banInfo.third)
                postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.BAN))
            }
            .onFailure { postSideEffect(ViewItSideEffect.UI.ShowSnackBar(SnackbarType.ERROR)) }
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
