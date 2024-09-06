package com.teamwable.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileAuthViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileAuthUiState>(ProfileAuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchAuthId()
    }

    private fun fetchAuthId() {
        viewModelScope.launch {
            userInfoRepository.getMemberId()
                .map { it.toLong() }
                .collectLatest { if (it != -1L) fetchProfileInfo(it) }
        }
    }

    private fun fetchProfileInfo(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileInfo(userId)
                .onSuccess {
                    _uiState.value = ProfileAuthUiState.Success(it)
                }
                .onFailure { _uiState.value = ProfileAuthUiState.Error(it.message.toString()) }
        }
    }

    fun updateProfile(nickname: String, imageUrl: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                when (currentState) {
                    is ProfileAuthUiState.Success -> {
                        val updatedProfile = currentState.profile.copy(
                            nickName = nickname,
                            profileImg = imageUrl,
                        )
                        currentState.copy(profile = updatedProfile)
                    }

                    else -> currentState
                }
            }
        }
    }
}

sealed interface ProfileAuthUiState {
    data object Loading : ProfileAuthUiState

    data class Success(val profile: Profile) : ProfileAuthUiState

    data class Error(val errorMessage: String) : ProfileAuthUiState
}
