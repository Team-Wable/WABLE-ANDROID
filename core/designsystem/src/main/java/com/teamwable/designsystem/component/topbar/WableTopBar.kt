package com.teamwable.designsystem.component.topbar

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WableAppBar(
    visibility: Boolean = false,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    resetToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (!visibility) return

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = WableTheme.colors.white,
        ),
        modifier = modifier.height(56.dp),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Icon(
                        painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_back_btn),
                        contentDescription = "",
                    )
                }
            }
        },
        actions = {
            if (canNavigateBack) {
                IconButton(
                    onClick = resetToLogin,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Icon(
                        painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_cancel_btn),
                        contentDescription = "Close",
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ImageDoubleButtonDialogPreview() {
    WableTheme {
        WableAppBar(canNavigateBack = true, visibility = true)
    }
}
