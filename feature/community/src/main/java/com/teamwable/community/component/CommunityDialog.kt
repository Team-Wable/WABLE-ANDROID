package com.teamwable.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.dialog.WableBaseDialog
import com.teamwable.designsystem.component.dialog.WableOneButtonDialog
import com.teamwable.designsystem.component.dialog.WableTwoButtonDialog
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType

@Composable
fun PreRegisterDialog(
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    WableTwoButtonDialog(
        dialogType = DialogType.PRE_REGISTER,
        onClick = onClick,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
fun PreRegisterCompletedDialog(
    name: String,
    onDismissRequest: () -> Unit,
) {
    WableBaseDialog(
        dialogType = DialogType.PRE_REGISTER_COMPLETED,
        name = name,
        onDismissRequest = onDismissRequest,
        imageContent = {
            Image(
                imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_commnity_dialog_check),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        },
    )
}

@Composable
fun PushNotificationDialog(
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    WableTwoButtonDialog(
        dialogType = DialogType.PUSH_NOTIFICATION,
        onClick = onClick,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
fun CopyCompletedDialog(
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    WableOneButtonDialog(
        dialogType = DialogType.COPY_COMPLETED,
        onClick = onClick,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityDialogPreview() {
    WableTheme {
//        PreRegisterCompletedDialog(
//            name = "김팀장",
//            onDismissRequest = {},
//        )
//        PreRegisterCompletedDialog()
//        PushNotificationDialog(
//            onClick = {},
//            onDismissRequest = {},
//        )
        CopyCompletedDialog(
            onClick = {},
            onDismissRequest = {},
        )
    }
}
