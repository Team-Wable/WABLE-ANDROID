package com.teamwable.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NewsRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(
    private val newsRepository: NewsRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val _gameTypeUiState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val gameTypeUiState = _gameTypeUiState.asStateFlow()

    private val _scheduleUiState = MutableStateFlow<UiState<List<NewsMatchModel>>>(UiState.Loading)
    val scheduleUiState = _scheduleUiState.asStateFlow()

    private val _rankUiState = MutableStateFlow<UiState<List<NewsRankModel>>>(UiState.Loading)
    val rankUiState = _rankUiState.asStateFlow()

    private val _newsNumberUiState = MutableStateFlow<UiState<Map<String, Int>>>(UiState.Loading)
    val newsNumberUiState = _newsNumberUiState.asStateFlow()

    init {
        getGameType()
        getNewsNumber()
    }

    private fun getGameType() {
        viewModelScope.launch {
            newsRepository.getGameType()
                .onSuccess { _gameTypeUiState.value = UiState.Success(it) }
                .onFailure { _gameTypeUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun getSchedule() {
        viewModelScope.launch {
            newsRepository.getSchedule()
                .onSuccess { _scheduleUiState.value = UiState.Success(it) }
                .onFailure { _scheduleUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun getRank() {
        viewModelScope.launch {
            newsRepository.getRank()
                .onSuccess { _rankUiState.value = UiState.Success(it) }
                .onFailure { _rankUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    private fun getNewsNumber() {
        viewModelScope.launch {
            newsRepository.getNumber()
                .onSuccess { _newsNumberUiState.value = UiState.Success(it) }
                .onFailure { _newsNumberUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    suspend fun getNewsNumberFromLocal() = userInfoRepository.getNewsNumber().first()

    suspend fun saveNewsNumber(newsNumber: Int) = userInfoRepository.saveNewsNumber(newsNumber)

    suspend fun getNoticeNumberFromLocal() = userInfoRepository.getNoticeNumber().first()

    suspend fun saveNoticeNumber(notice: Int) = userInfoRepository.saveNoticeNumber(notice)
}
