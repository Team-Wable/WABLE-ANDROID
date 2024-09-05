package com.teamwable.designsystem.extension.system

import android.content.Context
import android.content.Intent
import android.provider.Settings

// 앱 세팅으로 이동하는 함수
fun Context.navigateToAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = android.net.Uri.fromParts("package", this@navigateToAppSettings.packageName, null)
    }
    this.startActivity(intent)
}
