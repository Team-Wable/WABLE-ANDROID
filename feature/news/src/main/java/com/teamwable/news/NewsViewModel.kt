package com.teamwable.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import com.teamwable.news.model.NewsInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    private val _gameTypeUiState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val gameTypeUiState = _gameTypeUiState.asStateFlow()

    private val _scheduleUiState = MutableStateFlow<UiState<List<NewsMatchModel>>>(UiState.Loading)
    val scheduleUiState = _scheduleUiState.asStateFlow()

    private val _rankUiState = MutableStateFlow<UiState<List<NewsRankModel>>>(UiState.Loading)
    val rankUiState = _rankUiState.asStateFlow()

    val dummyNotice = listOf(
        NewsInfoModel(1, "와블 커뮤니티 업데이트 안내", "본문입니다 본문입니다 본문입니다 본문입니다 본문입니다 본문입니다 본문입니다 본문입니다 본문입니다", "www.11", "2024-01-10 11:47:18"),
        NewsInfoModel(2, "제목2", "내용2", null, "2024-06-12 20:00:37"),
        NewsInfoModel(3, "제목3", "내용3", "www.33", "2024-11-22 04:50:26"),
        NewsInfoModel(3, "제목4", "내용4", "www.33", "2024-11-22 04:50:26"),
        NewsInfoModel(3, "제목5", "내용5", "www.33", "2024-11-22 04:50:26"),
        NewsInfoModel(3, "제목6", "내용6", "www.33", "2024-11-22 04:50:26"),
        NewsInfoModel(3, "제목7", "내용7", "www.33", "2024-11-22 04:50:26"),
        NewsInfoModel(3, "제목8", "내용8", "www.33", "2024-11-22 04:50:26"),
    )

    init {
        getGameType()
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
}
