package com.teamwable.ui.extensions

import androidx.compose.runtime.snapshotFlow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

fun <T> MutableStateFlow<Set<T>>.addItem(item: T) {
    update { it + item }
}

fun <K, V> MutableStateFlow<Map<K, V>>.putItem(key: K, value: V) {
    update { it + (key to value) }
}

suspend fun awaitRefreshComplete(data: LazyPagingItems<*>) {
    snapshotFlow { data.loadState.refresh }
        .map { it is LoadState.NotLoading }
        .distinctUntilChanged()
        .filter { it }
        .timeout(5.seconds)
        .first()
}
