package com.teamwable.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.PostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel
@Inject constructor(
    private val postingRepository: PostingRepository,
) : ViewModel() {
    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    private val _postingUiState = MutableSharedFlow<UiState<Unit>>()
    val postingUiState = _postingUiState.asSharedFlow()

    fun setPhotoUri(uri: String?) {
        _photoUri.value = uri
    }

    fun posting(title: String, content: String, imageUri: String?) =
        viewModelScope.launch {
            _postingUiState.emit(UiState.Loading)
            postingRepository.postingMultiPart(title, content, imageUri)
                .onSuccess { _postingUiState.emit(UiState.Success(it)) }
                .onFailure { _postingUiState.emit(UiState.Failure(it.message.toString())) }
        }
}
