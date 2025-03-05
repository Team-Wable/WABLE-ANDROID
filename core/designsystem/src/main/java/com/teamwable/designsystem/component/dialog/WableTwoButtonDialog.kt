package com.teamwable.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.teamwable.designsystem.component.button.WableTwoButtons
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType

@Composable
fun WableTwoButtonDialog(
    dialogType: DialogType,
    name: String = "",
    onClick: () -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = WableTheme.colors.white,
            ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DialogContent(
                    dialogType = dialogType,
                    contentName = name,
                )
                Spacer(modifier = Modifier.height(32.dp))
                WableTwoButtons(
                    startButtonText = stringResource(id = dialogType.cancelButtonText),
                    endButtonText = stringResource(id = dialogType.buttonText),
                    onStartButtonClick = onDismissRequest,
                    onEndButtonClick = onClick,
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WableTwoButtonDialogPreview() {
    WableTheme {
        WableTwoButtonDialog(
            dialogType = DialogType.PUSH_NOTIFICATION,
            name = "홍길동",
            onClick = {},
        )
    }
}
