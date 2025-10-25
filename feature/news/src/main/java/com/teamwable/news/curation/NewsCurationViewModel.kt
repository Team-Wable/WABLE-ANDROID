package com.teamwable.news.curation

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teamwable.common.base.BaseViewModel
import com.teamwable.common.base.EmptyState
import com.teamwable.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class NewsCurationViewModel @Inject constructor(
    newsRepository: NewsRepository,
) : BaseViewModel<NewsCurationIntent, EmptyState, NewsCurationSideEffect>(
        initialState = EmptyState,
    ) {
    val curationPagingFlow = newsRepository.getCurationInfo()
        .cachedIn(viewModelScope)
        .catch {
            postSideEffect(NewsCurationSideEffect.UI.ShowSnackBar(it.cause))
        }

    override fun onIntent(intent: NewsCurationIntent) {
        when (intent) {
            is NewsCurationIntent.ClickLink -> onLinkClick(intent.url)
            NewsCurationIntent.PullToRefresh -> onRefreshCuration()
        }
    }

    private fun onLinkClick(url: String) {
        postSideEffect(NewsCurationSideEffect.Navigation.ToUrl(url))
    }

    private fun onRefreshCuration() {
        postSideEffect(NewsCurationSideEffect.UI.Refresh)
    }
}
