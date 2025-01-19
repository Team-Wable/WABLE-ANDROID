package com.teamwable.news.news

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teamwable.common.base.BaseViewModel
import com.teamwable.common.base.EmptyState
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.news.contract.NewsInfoIntent
import com.teamwable.news.news.contract.NewsInfoSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class NewsNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : BaseViewModel<NewsInfoIntent, EmptyState, NewsInfoSideEffect>(
        initialState = EmptyState,
    ) {
    val newsPagingFlow = newsRepository.getNewsInfo()
        .cachedIn(viewModelScope)
        .catch {
            postSideEffect(NewsInfoSideEffect.ShowSnackBar(it.message.orEmpty()))
        }

    override fun onIntent(intent: NewsInfoIntent) {
        when (intent) {
            is NewsInfoIntent.ItemClick -> onItemClick(intent.newsInfoModel)
        }
    }

    private fun onItemClick(newsInfoModel: NewsInfoModel) {
        postSideEffect(NewsInfoSideEffect.NavigateToDetail(newsInfoModel))
    }
}
