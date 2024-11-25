package com.teamwable.news.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.news.model.NewsInfoSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<NewsInfoSideEffect>()
    val sideEffect: SharedFlow<NewsInfoSideEffect> get() = _sideEffect.asSharedFlow()

    val newsPagingFlow = newsRepository.getNewsInfo().cachedIn(viewModelScope)

    fun onItemClick(newsInfoModel: NewsInfoModel) {
        viewModelScope.launch {
            _sideEffect.emit(NewsInfoSideEffect.NavigateToDetail(newsInfoModel))
        }
    }
}
