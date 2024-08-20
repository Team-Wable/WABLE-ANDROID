package com.teamwable.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.auth.model.LoginSideEffect
import com.teamwable.data.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _loginSideEffect: MutableSharedFlow<LoginSideEffect> = MutableSharedFlow()
    val loginSideEffect: SharedFlow<LoginSideEffect>
        get() = _loginSideEffect.asSharedFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    fun observeAutoLogin() {
        viewModelScope.launch {
            userInfoRepository.getAutoLogin().collectLatest { isAutoLogin ->
                if (isAutoLogin) {
                    _loginSideEffect.emit(LoginSideEffect.NavigateToMain)
                }
            }
        }
    }

    fun saveIsAutoLogin(input: Boolean) {
        viewModelScope.launch {
            userInfoRepository.saveAutoLogin(input)
        }
    }

    fun showLoginDialog(show: Boolean) {
        _showDialog.update { show }
    }
}
