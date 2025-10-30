package com.teamwable.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.Profile
import com.teamwable.ui.type.ProfileUserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileMemberViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileMemberUiState>(ProfileMemberUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var isAdmin = false

    init {
        fetchIsAdmin()
    }

    fun fetchProfileInfo(userId: Long) {
        viewModelScope.launch {
            profileRepository.getProfileInfo(userId)
                .onSuccess {
                    _uiState.value = ProfileMemberUiState.Success(it)
                }
                .onFailure { _uiState.value = ProfileMemberUiState.Error(it.message.toString()) }
        }
    }

    private fun fetchIsAdmin() = viewModelScope.launch {
        userInfoRepository.getIsAdmin()
            .collectLatest { isAdmin = it }
    }

    fun fetchUserType(): ProfileUserType = when (isAdmin) {
        true -> ProfileUserType.ADMIN
        false -> ProfileUserType.MEMBER
    }
}

sealed interface ProfileMemberUiState {
    data object Loading : ProfileMemberUiState

    data class Success(val profile: Profile) : ProfileMemberUiState

    data class Error(val errorMessage: String) : ProfileMemberUiState
}
