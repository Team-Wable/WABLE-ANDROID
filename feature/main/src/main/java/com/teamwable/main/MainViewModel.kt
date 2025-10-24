package com.teamwable.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.NewsRepository
import com.teamwable.data.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _redDotUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val redDotUiState = _redDotUiState.asStateFlow()

    init {
        validateNewsRedDot()
    }

    fun validateNewsRedDot() {
        viewModelScope.launch {
            val serverNoticeNumber = newsRepository.getNumber().getOrNull()?.get("notice") ?: -1
            val serverCurationId = newsRepository.getCurationNumber().getOrNull() ?: -1

            combine(
                userInfoRepository.getNoticeNumber(),
                userInfoRepository.getCurationNumber(),
            ) { localNoticeNumber, localCurationId ->
                serverNoticeNumber > localNoticeNumber || serverCurationId != localCurationId
            }.collect { isRedDot ->
                _redDotUiState.value = UiState.Success(isRedDot)
            }
        }
    }
}
