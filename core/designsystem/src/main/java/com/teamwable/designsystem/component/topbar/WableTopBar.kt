package com.teamwable.designsystem.component.topbar

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WableAppBar(
    visibility: Boolean = false,
    canNavigateBack: Boolean,
    canClose: Boolean = true,
    darkTheme: Boolean = false,
    title: String = "",
    navigateUp: () -> Unit = {},
    resetToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (!visibility) return
    val iconColor = if (darkTheme) WableTheme.colors.white else WableTheme.colors.black

    TopAppBar(
        title = {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = WableTheme.typography.body01,
                    color = iconColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = if (darkTheme) WableTheme.colors.black else WableTheme.colors.white,
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
                        tint = iconColor,
                    )
                }
            }
        },
        actions = {
            if (canNavigateBack && canClose) {
                IconButton(
                    onClick = resetToLogin,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Icon(
                        painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_cancel_btn),
                        contentDescription = "Close",
                        tint = iconColor,
                    )
                }
            } else {
                IconButton(
                    onClick = {},
                    modifier = Modifier.fillMaxHeight(),
                ) {}
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ImageDoubleButtonDialogPreview() {
    WableTheme {
        WableAppBar(
            canNavigateBack = true,
            visibility = true,
            darkTheme = false,
            title = "타이틀",
        )
    }
}
