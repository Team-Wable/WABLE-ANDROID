package com.teamwable.main

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateHandler(private val appUpdateManager: AppUpdateManager) {
    fun checkForAppUpdate(onUpdateAvailable: (AppUpdateInfo) -> Unit) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                onUpdateAvailable(appUpdateInfo)
        }
    }

    fun startUpdate(appUpdateInfo: AppUpdateInfo, activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        val appUpdateType = if (checkIsImmediate(appUpdateInfo)) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            activityResultLauncher,
            AppUpdateOptions.newBuilder(appUpdateType).build(),
        )
    }

    fun checkIsImmediate(appUpdateInfo: AppUpdateInfo): Boolean {
        val isFirstCodeDifferent = (BuildConfig.VERSION_CODE / 100) != (appUpdateInfo.availableVersionCode() / 100)
        val isSecondCodeDifferent = ((BuildConfig.VERSION_CODE % 100) / 10) != ((appUpdateInfo.availableVersionCode() % 100) / 10)
        return isFirstCodeDifferent || isSecondCodeDifferent
    }
}
