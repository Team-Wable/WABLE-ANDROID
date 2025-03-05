package com.teamwable.designsystem.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamwable.designsystem.component.button.BigButtonDefaults
import com.teamwable.designsystem.component.button.BigButtonStyle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.button.WableTwoButtons
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType

@Composable
fun WableOneButtonDialog(
    dialogType: DialogType = DialogType.LOGIN,
    name: String = "",
    buttonStyle: BigButtonStyle = BigButtonDefaults.dialogButtonStyle(),
    onDismissRequest: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    WableBaseDialog(
        dialogType = dialogType,
        name = name,
        onDismissRequest = onDismissRequest,
        buttonContent = {
            WableButton(
                text = stringResource(id = dialogType.buttonText),
                buttonStyle = buttonStyle,
                onClick = onClick,
            )
        },
    )
}

@Composable
fun WableTwoButtonDialog(
    dialogType: DialogType = DialogType.PRE_REGISTER,
    buttonStyle: BigButtonStyle = BigButtonDefaults.dialogTwoButtonStyle(),
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {
    WableBaseDialog(
        dialogType = dialogType,
        onDismissRequest = onDismissRequest,
        buttonContent = {
            WableTwoButtons(
                cancelButtonText = stringResource(id = dialogType.cancelButtonText),
                buttonText = stringResource(id = dialogType.buttonText),
                buttonStyle = buttonStyle,
                onCancel = onDismissRequest,
                onClick = onClick,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun WableButtonDialogPreview() {
    WableTheme {
        WableTwoButtonDialog(
            onClick = {},
            onDismissRequest = {},
        )
    }
}
