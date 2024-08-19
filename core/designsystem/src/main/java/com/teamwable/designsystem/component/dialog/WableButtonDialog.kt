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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.teamwable.designsystem.R
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun ImageDoubleButtonDialog(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Dialog(onDismissRequest = onClick) {
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

                Text(
                    text = title,
                    style = WableTheme.typography.head01,
                    color = WableTheme.colors.black,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = WableTheme.typography.body02,
                    color = WableTheme.colors.gray700,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                WableButton(
                    text = buttonText,
                    onClick = onClick,
                    enabled = true,
                    textStyle = WableTheme.typography.body01,
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDoubleButtonDialogPreview() {
    WableTheme {
        ImageDoubleButtonDialog(
            title = stringResource(R.string.dialog_login_title),
            description = stringResource(R.string.dialog_login_description) +
                "더 건강하고 즐거운 커뮤니티를 만들어 나가는데 함께 노력해주실거죠?",
            buttonText = stringResource(R.string.dialog_login_btn_text),
            onClick = {},
        )
    }
}
