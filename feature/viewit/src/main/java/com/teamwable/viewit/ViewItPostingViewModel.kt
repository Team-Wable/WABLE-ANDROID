package com.teamwable.viewit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ViewItRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewItPostingViewModel @Inject constructor(
    private val viewItRepository: ViewItRepository,
) : ViewModel() {
    private val _event = MutableSharedFlow<ViewItPostingSideEffect>()
    val event = _event.asSharedFlow()

    fun getLinkInfo(link: String) = viewModelScope.launch {
        viewItRepository.getLinkInfo(link)
            .onSuccess {
            }
            .onFailure { _event.emit(ViewItPostingSideEffect.ShowErrorMessage(it.message ?: return@launch)) }
    }
}

sealed interface ViewItPostingSideEffect {
    data class ShowErrorMessage(val message: String) : ViewItPostingSideEffect
}
