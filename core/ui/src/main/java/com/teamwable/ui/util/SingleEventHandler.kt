package com.teamwable.ui.util

class SingleEventHandler private constructor() {
    private val lastCalls = mutableMapOf<String, Long>()

    fun canProceed(key: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastCallTime = lastCalls[key] ?: 0L
        return if (currentTime - lastCallTime >= THROTTLE_DELAY) {
            lastCalls[key] = currentTime
            true
        } else {
            false
        }
    }

    companion object {
        private const val THROTTLE_DELAY = 2000L

        fun from(): SingleEventHandler = SingleEventHandler()
    }
}
