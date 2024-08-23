package com.teamwable.onboarding.sharedviewmodel

import com.teamwable.model.auth.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalUserManager @Inject constructor() {
    private val _userModel = MutableStateFlow(UserModel())
    val userModel: StateFlow<UserModel> get() = _userModel

    fun updateUserModel(userModel: UserModel) {
        _userModel.value = userModel
    }

    fun reset() {
        _userModel.value = UserModel() // 상태 초기화
    }
}
