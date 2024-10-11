package com.teamwable.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.SnackBarType

@Composable
fun WableSnackBarPopUp(
    isVisible: Boolean,
    snackBarType: SnackBarType = SnackBarType.LOADING,
    onDismissRequest: () -> Unit = {},
) {
    if (isVisible) {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                excludeFromSystemGesture = true,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WableTheme.colors.white.copy(alpha = 0.5f)),
            ) {
                WableSnackBar(
                    snackBarType = snackBarType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WableSnackBarPopUpPreview() {
    WableTheme {
        WableSnackBarPopUp(
            isVisible = true,
            snackBarType = SnackBarType.LOADING,
        )
    }
}
