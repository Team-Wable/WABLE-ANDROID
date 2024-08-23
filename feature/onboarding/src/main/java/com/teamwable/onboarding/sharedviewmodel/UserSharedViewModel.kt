package com.teamwable.onboarding.sharedviewmodel

import androidx.lifecycle.ViewModel
import com.teamwable.model.auth.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSharedViewModel @Inject constructor(
    private val globalUserManager: GlobalUserManager,
) : ViewModel() {
    val userModel = globalUserManager.userModel

    fun updateUserModel(userModel: UserModel) {
        globalUserManager.updateUserModel(userModel)
    }

    override fun onCleared() {
        super.onCleared()
        globalUserManager.reset() // ViewModel이 소멸될 때 상태 초기화
    }
}
