package com.teamwable.ui.component.feedimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.domain.usecase.SaveImageUseCase
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedImageViewModel @Inject constructor(
    private val saveImageUseCase: SaveImageUseCase,
) : ViewModel() {
    private val _sideEffect: Channel<FeedImageSideEffect> = Channel(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    /**
     * Initiates saving an image from the provided URL and emits UI side effects to indicate progress, completion, or error.
     *
     * Emits a snackbar side effect to notify the user when the save operation starts, completes successfully, or fails.
     *
     * @param imageUrl The URL of the image to be saved.
     */
    fun saveImage(imageUrl: String) {
        viewModelScope.launch {
            _sideEffect.send(FeedImageSideEffect.ShowSnackBar(SnackbarType.VIEW_IT_ING))
            saveImageUseCase(imageUrl)
                .onSuccess {
                    _sideEffect.send(FeedImageSideEffect.ShowSnackBar(SnackbarType.VIEW_IT_COMPLETE))
                }
                .onFailure {
                    _sideEffect.send(FeedImageSideEffect.ShowSnackBar(SnackbarType.ERROR, it))
                }
        }
    }
}

sealed interface FeedImageSideEffect {
    data class ShowSnackBar(
        val type: SnackbarType,
        val throwable: Throwable? = null,
    ) : FeedImageSideEffect
}
