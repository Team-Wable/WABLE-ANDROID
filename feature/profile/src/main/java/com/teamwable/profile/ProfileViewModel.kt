package com.teamwable.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.profile.MemberDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _memberDataUiState = MutableStateFlow<UiState<MemberDataModel>>(UiState.Empty)
    val memberDataUiState = _memberDataUiState.asStateFlow()

    fun getMemberData() {
        viewModelScope.launch {
            _memberDataUiState.value = UiState.Loading
            profileRepository.getMemberData()
                .onSuccess { _memberDataUiState.value = UiState.Success(it) }
                .onFailure { _memberDataUiState.value = UiState.Failure(it.message.toString()) }
        }
    }
}
