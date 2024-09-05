package com.teamwable.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.teamwable.auth.model.LoginSideEffect
import com.teamwable.data.repository.AuthRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.network.Error
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
    private val authRepository: AuthRepository,
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

    fun showLoginDialog(show: Boolean) {
        _showDialog.update { show }
    }

    fun startKaKaoLogin(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleKaKaoLoginResult(token, error)
            }
        } else {
            // 카카오 계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleKaKaoLoginResult(token, error)
            }
        }
    }

    private fun handleKaKaoLoginResult(token: OAuthToken?, error: Throwable?) {
        viewModelScope.launch {
            when {
                token != null -> kakaoLoginSuccess(token) // 성공
                error != null -> kakaoLoginFailure(error) // 실패
            }
        }
    }

    private fun kakaoLoginFailure(error: Throwable) {
        viewModelScope.launch {
            when {
                error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                    _loginSideEffect.emit(
                        LoginSideEffect.ShowSnackBar(Error.ApiError("카카오 로그인이 취소되었습니다")),
                    )

                else -> _loginSideEffect.emit(LoginSideEffect.ShowSnackBar(Error.ApiError("카카오 로그인에 실패했습니다")))
            }
        }
    }

    private fun kakaoLoginSuccess(token: OAuthToken) {
        viewModelScope.launch {
            showLoginDialog(false)
            postLogin(token.accessToken)
        }
    }

    private fun postLogin(token: String) {
        viewModelScope.launch {
            authRepository.postLogin(KAKAO, BEARER + token)
                .onSuccess { response ->
                    saveAccessToken(response.accessToken)
                    saveRefreshToken(response.refreshToken)
                    saveMemberId(response.memberId)
                    checkIsNewUser(response.isNewUser)
                }.onFailure {
                    _loginSideEffect.emit(LoginSideEffect.ShowSnackBar(it))
                }
        }
    }

    private fun saveAccessToken(token: String) { // 홈에서 서버 통신 안되면 여길 suspend로 바꿔주세요
        viewModelScope.launch {
            userInfoRepository.saveAccessToken(BEARER + token)
        }
    }

    private fun saveRefreshToken(token: String) {
        viewModelScope.launch {
            userInfoRepository.saveRefreshToken(BEARER + token)
        }
    }

    private fun saveMemberId(input: Int) {
        viewModelScope.launch {
            userInfoRepository.saveMemberId(input)
        }
    }

    private fun checkIsNewUser(newUser: Boolean) {
        viewModelScope.launch {
            if (newUser) saveIsAutoLogin(true)
            else _loginSideEffect.emit(LoginSideEffect.NavigateToFirstLckWatch)
        }
    }

    private fun saveIsAutoLogin(input: Boolean) {
        viewModelScope.launch {
            userInfoRepository.saveAutoLogin(input)
        }
    }

    companion object {
        private const val BEARER = "Bearer "
        private const val KAKAO = "KAKAO"
    }
}
