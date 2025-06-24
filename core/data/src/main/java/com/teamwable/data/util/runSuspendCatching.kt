package com.teamwable.data.util

import com.teamwable.network.util.toCustomError
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

inline fun <T, R> T.runSuspendCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: TimeoutCancellationException) {
        Result.failure(t)
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun <T, R> T.runHandledCatching(block: T.() -> R): Result<R> {
    return runSuspendCatching(block).fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toCustomError()) },
    )
}
