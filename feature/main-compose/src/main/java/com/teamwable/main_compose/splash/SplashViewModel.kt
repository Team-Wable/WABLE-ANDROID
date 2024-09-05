package com.teamwable.main_compose.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.main_compose.splash.model.SplashSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SplashSideEffect>()
    val sideEffect: SharedFlow<SplashSideEffect> = _sideEffect.asSharedFlow()

    fun observeAutoLogin() {
        viewModelScope.launch {
            userInfoRepository.getAutoLogin().collectLatest { isAutoLogin ->
                if (isAutoLogin) _sideEffect.emit(SplashSideEffect.NavigateToHome)
                else _sideEffect.emit(SplashSideEffect.NavigateToLogin)
            }
        }
    }
}
