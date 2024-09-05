package com.teamwable.designsystem.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamwable.designsystem.R
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun PermissionAppSettingsDialog(
    onClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        containerColor = WableTheme.colors.white,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(R.string.permission_app_setting_dialog_title),
                color = WableTheme.colors.black,
                style = WableTheme.typography.head01,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_app_setting_dialog_description),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray700,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onClick()
                },
            ) {
                Text(
                    text = stringResource(R.string.permission_app_setting_dialog_moving),
                    style = WableTheme.typography.body02,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
            ) {
                Text(
                    text = stringResource(R.string.permission_app_setting_dialog_cancel),
                    color = WableTheme.colors.gray500,
                    style = WableTheme.typography.body02,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    WableTheme {
        PermissionAppSettingsDialog()
    }
}
