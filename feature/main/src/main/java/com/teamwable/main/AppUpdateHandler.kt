package com.teamwable.main

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateHandler(private val context: Context) {
    private lateinit var appUpdateManager: AppUpdateManager

    fun checkForAppUpdate(activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                showUpdateDialog(appUpdateInfo, activityResultLauncher)
        }
    }

    private fun showUpdateDialog(appUpdateInfo: AppUpdateInfo, activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.label_in_app_update_title))
            .setMessage(context.getString(R.string.label_in_app_update_content))
            .setPositiveButton(context.getString(R.string.label_in_app_update_yes)) { dialog, _ ->
                dialog.dismiss()
                startUpdate(appUpdateInfo, activityResultLauncher)
            }
            .setNegativeButton(context.getString(R.string.label_in_app_update_next)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            activityResultLauncher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
        )
    }

    fun resumeUpdateIfNeeded(activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // In-app update가 이미 실행 중이면, 업데이트를 다시 시작
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                )
            }
        }
    }
}
