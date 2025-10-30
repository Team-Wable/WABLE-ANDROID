package com.teamwable.designsystem.component.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableCheckBoxWithText(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    textStyle: TextStyle = WableTheme.typography.body02,
    textColor: Color = WableTheme.colors.black,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WableCheckBox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WableCheckBoxPreview() {
    var isChecked by remember { mutableStateOf(false) }

    WableTheme {
        WableCheckBoxWithText(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            text = "[필수] 개인정보 수집 및 이용동의",
        )
    }
}
