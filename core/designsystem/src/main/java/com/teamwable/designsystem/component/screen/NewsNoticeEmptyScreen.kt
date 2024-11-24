package com.teamwable.designsystem.component.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun NewsNoticeEmptyScreen(@StringRes emptyTxt: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(emptyTxt),
            color = WableTheme.colors.gray500,
            style = WableTheme.typography.body02,
        )
    }
}
