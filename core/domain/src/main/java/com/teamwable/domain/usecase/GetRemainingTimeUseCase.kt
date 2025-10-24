package com.teamwable.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class GetRemainingTimeUseCase @Inject constructor() {
    operator fun invoke(): Flow<Pair<Long, Long>> = minuteTickerFlow()
        .onStart { emit(calculateRemainingTime()) }

    private fun minuteTickerFlow(): Flow<Pair<Long, Long>> = flow {
        val now = LocalDateTime.now()

        val initialDelay = 60_000L - (now.second * 1000L)
        delay(initialDelay)

        while (true) {
            emit(calculateRemainingTime())
            delay(60_000L)
        }
    }

    private fun calculateRemainingTime(): Pair<Long, Long> {
        val now = LocalDateTime.now()
        val midnight = LocalDate.now().plusDays(1).atStartOfDay()
        val duration = Duration.between(now, midnight)

        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return hours to minutes
    }
}
