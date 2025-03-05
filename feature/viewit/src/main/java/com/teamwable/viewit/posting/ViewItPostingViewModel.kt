package com.teamwable.viewit.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ViewItRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewItPostingViewModel @Inject constructor(
    private val viewItRepository: ViewItRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewItPostingUiState>(ViewItPostingUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ViewItPostingSideEffect>()
    val event = _event.asSharedFlow()

    fun postViewIt(link: String, content: String) = viewModelScope.launch {
        viewItRepository.postViewIt(link, content)
            .onSuccess {
                Timber.e(it.toString())
                _uiState.value = ViewItPostingUiState.Success
            }
            .onFailure {
                _event.emit(
                    ViewItPostingSideEffect.ShowErrorMessage(
                        it.message ?: return@launch,
                    ),
                )
            }
    }
}

sealed interface ViewItPostingUiState {
    data object Loading : ViewItPostingUiState

    data object Success : ViewItPostingUiState
}

sealed interface ViewItPostingSideEffect {
    data class ShowErrorMessage(val message: String) : ViewItPostingSideEffect
}
