package com.teamwable.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileMemberViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileMemberUiState>(ProfileMemberUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchProfileInfo(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileInfo(userId)
                .onSuccess {
                    _uiState.value = ProfileMemberUiState.Success(it)
                }
                .onFailure { _uiState.value = ProfileMemberUiState.Error(it.message.toString()) }
        }
    }
}

sealed interface ProfileMemberUiState {
    data object Loading : ProfileMemberUiState

    data class Success(val profile: Profile) : ProfileMemberUiState

    data class Error(val errorMessage: String) : ProfileMemberUiState
}
