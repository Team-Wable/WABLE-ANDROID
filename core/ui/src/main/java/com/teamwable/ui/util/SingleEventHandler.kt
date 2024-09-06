package com.teamwable.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SingleEventHandler private constructor() {
    private lateinit var debounceJob: Job

    fun debounce(
        delayTime: Long = DEBOUNCE_DELAY,
        coroutineScope: CoroutineScope,
        event: () -> Unit,
    ) {
        if (::debounceJob.isInitialized) debounceJob.cancel()
        debounceJob = coroutineScope.launch {
            delay(delayTime)
            event()
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L

        fun from(): SingleEventHandler = SingleEventHandler()
    }
}
