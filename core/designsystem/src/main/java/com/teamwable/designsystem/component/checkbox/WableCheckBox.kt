package com.teamwable.designsystem.component.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawableResId =
        if (checked) com.teamwable.common.R.drawable.ic_sign_up_checkbox_selected_gray900
        else com.teamwable.common.R.drawable.ic_sign_up_checkbox_unselected

    Box(
        modifier = Modifier
            .noRippleClickable { onCheckedChange(!checked) },
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WableCustomTermsPreview() {
    var isChecked by remember { mutableStateOf(false) }

    WableTheme {
        WableCheckBox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
        )
    }
}
