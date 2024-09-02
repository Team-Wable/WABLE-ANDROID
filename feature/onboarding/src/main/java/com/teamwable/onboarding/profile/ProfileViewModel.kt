package com.teamwable.onboarding.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect: SharedFlow<ProfileSideEffect> = _sideEffect.asSharedFlow()

    private val _selectedImageUri = MutableStateFlow<String?>(null)
    val selectedImageUri: StateFlow<String?> = _selectedImageUri

    fun requestImagePicker() {
        viewModelScope.launch {
            _sideEffect.emit(ProfileSideEffect.RequestImagePicker)
        }
    }

    fun onImageSelected(imageUri: String?) {
        viewModelScope.launch {
            _selectedImageUri.update { imageUri }
        }
    }
}
