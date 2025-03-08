package com.teamwable.community.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.teamwable.community.model.CommunityIntent
import com.teamwable.community.model.CommunityState
import com.teamwable.designsystem.type.DialogType

@Composable
fun HandleDialog(
    state: CommunityState,
    onDismissRequest: () -> Unit = {},
    onPreRegisterBtnClick: () -> Unit = {},
    onPreRegisterCancelBtnClick: () -> Unit = {},
    onPushBtnClick: () -> Unit = {},
    onIntent: (CommunityIntent) -> Unit = {},
) {
    when (state.dialogType) {
        DialogType.PRE_REGISTER ->
            PreRegisterDialog(
                onClick = onPreRegisterBtnClick,
                onDismissRequest = onPreRegisterCancelBtnClick,
            )

        DialogType.PRE_REGISTER_COMPLETED -> {
            PreRegisterCompletedDialog(
                name = state.preRegisterTeamName,
                onDismissRequest = onDismissRequest,
            )
            CheckSelfPushPermission(
                state = state,
                context = LocalContext.current,
                onIntent = onIntent,
            )
        }

        DialogType.PUSH_NOTIFICATION ->
            PushNotificationDialog(
                onClick = onPushBtnClick,
                onDismissRequest = onDismissRequest,
            )

        DialogType.COPY_COMPLETED ->
            CopyCompletedDialog(
                onClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )

        else -> Unit
    }
}

@Composable
private fun CheckSelfPushPermission(
    state: CommunityState,
    context: Context,
    onIntent: (CommunityIntent) -> Unit,
) {
    LaunchedEffect(state.dialogType) {
        if (state.dialogType == DialogType.PRE_REGISTER_COMPLETED) {
            val permissionStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            } else {
                PackageManager.PERMISSION_GRANTED
            }

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                onIntent(CommunityIntent.UpdatePhotoPermission(true))
            }
        }
    }
}
