package com.teamwable.onboarding.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.regex.NicknameValidationUseCase
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
class ProfileViewModel @Inject constructor(
    private val nicknameValidationUseCase: NicknameValidationUseCase,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect: SharedFlow<ProfileSideEffect> = _sideEffect.asSharedFlow()

    private val _selectedImageUri = MutableStateFlow<String?>(null)
    val selectedImageUri: StateFlow<String?> = _selectedImageUri

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _textFieldType = MutableStateFlow(NicknameType.DEFAULT)
    val textFieldType: StateFlow<NicknameType> = _textFieldType

    fun navigateToAgreeTerms() {
        viewModelScope.launch {
            _sideEffect.emit(ProfileSideEffect.NavigateToAgreeTerms)
        }
    }

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

    fun onNicknameChanged(newNickname: String) {
        _nickname.value = newNickname
        validateNickname(newNickname)
    }

    private fun validateNickname(nickname: String) {
        viewModelScope.launch {
            _textFieldType.update { nicknameValidationUseCase(nickname) }
        }
    }
}
