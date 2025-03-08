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
        val appUpdateType = when (checkUpdateType(appUpdateInfo)) {
            UpdateType.MAJOR -> AppUpdateType.IMMEDIATE
            UpdateType.MINOR, UpdateType.PATCH -> AppUpdateType.FLEXIBLE
            UpdateType.NONE -> return
        }

        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            activityResultLauncher,
            AppUpdateOptions.newBuilder(appUpdateType).build(),
        )
    }

    fun checkUpdateType(appUpdateInfo: AppUpdateInfo): UpdateType {
        val currentVersion = BuildConfig.VERSION_CODE
        val availableVersion = appUpdateInfo.availableVersionCode()

        val currentMajor = currentVersion / 10000
        val currentMinor = (currentVersion % 10000) / 100
        val currentPatch = currentVersion % 100

        val availableMajor = availableVersion / 10000
        val availableMinor = (availableVersion % 10000) / 100
        val availablePatch = availableVersion % 100

        return when {
            currentMajor != availableMajor -> UpdateType.MAJOR
            currentMinor != availableMinor -> UpdateType.MINOR
            currentPatch != availablePatch -> UpdateType.PATCH
            else -> UpdateType.NONE
        }
    }
}

enum class UpdateType {
    MAJOR, // 버전 첫 번째 자리수 변경
    MINOR, // 버전 두 번째 자리수 변경
    PATCH, // 버전 세 번째 자리수 변경
    NONE,
}
