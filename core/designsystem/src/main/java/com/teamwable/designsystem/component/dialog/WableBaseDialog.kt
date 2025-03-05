package com.teamwable.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.teamwable.designsystem.R
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType

@Composable
fun WableBaseDialog(
    dialogType: DialogType,
    name: String = "",
    onDismissRequest: () -> Unit = {},
    imageContent: @Composable () -> Unit = {},
    buttonContent: @Composable () -> Unit = {},
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
                Spacer(modifier = Modifier.height(32.dp))
                imageContent()
                DialogContent(
                    dialogType = dialogType,
                    contentName = name,
                )
                Spacer(modifier = Modifier.height(32.dp))
                buttonContent()
                if (buttonContent != {}) Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun DialogContent(
    dialogType: DialogType,
    contentName: String,
) {
    Text(
        text = stringResource(id = dialogType.title),
        style = WableTheme.typography.head01,
        color = WableTheme.colors.black,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(8.dp))

    val descriptionText = when (dialogType) {
        DialogType.WELLCOME -> {
            val rawDescription = stringResource(id = R.string.dialog_wellcome_description)
            String.format(rawDescription, contentName)
        }

        DialogType.REGISTER_COMPLETED -> {
            val rawDescription = stringResource(id = R.string.dialog_register_completed_description)
            String.format(rawDescription, contentName)
        }

        else -> stringResource(id = dialogType.description)
    }

    Text(
        text = descriptionText,
        style = WableTheme.typography.body02,
        color = WableTheme.colors.gray700,
        textAlign = TextAlign.Center,
    )
}
