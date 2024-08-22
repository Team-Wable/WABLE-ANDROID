package com.teamwable.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    private val _gameTypeUiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val gameTypeUiState = _gameTypeUiState.asStateFlow()

    private val _scheduleUiState = MutableStateFlow<UiState<List<NewsMatchModel>>>(UiState.Empty)
    val scheduleUiState = _scheduleUiState.asStateFlow()

    private val _rankUiState = MutableStateFlow<UiState<List<NewsRankModel>>>(UiState.Empty)
    val rankUiState = _rankUiState.asStateFlow()

    init {
        getGameType()
    }

    private fun getGameType() {
        viewModelScope.launch {
            _gameTypeUiState.value = UiState.Loading
            newsRepository.getGameType()
                .onSuccess { _gameTypeUiState.value = UiState.Success(it) }
                .onFailure { _gameTypeUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun getSchedule() {
        viewModelScope.launch {
            _scheduleUiState.value = UiState.Loading
            newsRepository.getSchedule()
                .onSuccess { _scheduleUiState.value = UiState.Success(it) }
                .onFailure { _scheduleUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun getRank() {
        viewModelScope.launch {
            _rankUiState.value = UiState.Loading
            newsRepository.getRank()
                .onSuccess { _rankUiState.value = UiState.Success(it) }
                .onFailure { _rankUiState.value = UiState.Failure(it.message.toString()) }
        }
    }
}
