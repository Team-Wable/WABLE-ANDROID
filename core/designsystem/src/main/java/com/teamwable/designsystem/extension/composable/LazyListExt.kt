package com.teamwable.designsystem.extension.composable

import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.scrollToTop() {
    animateScrollToItem(0)
}
