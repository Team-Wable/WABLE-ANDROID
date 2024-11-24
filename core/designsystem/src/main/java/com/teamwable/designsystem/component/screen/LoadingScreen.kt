package com.teamwable.designsystem.component.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.paging.WablePagingSpinner

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        WablePagingSpinner()
    }
}
