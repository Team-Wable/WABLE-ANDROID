package com.teamwable.ui.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<Set<T>>.addItem(item: T) {
    update { it + item }
}

fun <K, V> MutableStateFlow<Map<K, V>>.putItem(key: K, value: V) {
    update { it + (key to value) }
}
