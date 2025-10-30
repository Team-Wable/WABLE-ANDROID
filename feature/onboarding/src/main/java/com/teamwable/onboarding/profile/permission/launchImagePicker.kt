package com.teamwable.onboarding.profile.permission

import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

fun Context.launchImagePicker(
    galleryLauncher: ActivityResultLauncher<String>,
    photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        // API 33 미만: 갤러리 실행
        galleryLauncher.launch("image/*")
    } else {
        // API 33 이상: 포토피커 실행 (이미지만 선택)
        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}
