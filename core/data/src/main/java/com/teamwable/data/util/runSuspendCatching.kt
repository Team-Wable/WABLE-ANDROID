package com.teamwable.data.util

import com.teamwable.network.util.toCustomError
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes the given block on the receiver and returns its result as a [Result], capturing exceptions.
 *
 * Catches [TimeoutCancellationException] and other exceptions, wrapping them in a failure result.
 * Rethrows [CancellationException] to allow coroutine cancellation to propagate.
 *
 * @return [Result.success] with the block's result if successful, or [Result.failure] with the caught exception.
 */
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

/**
 * Executes a block on the receiver and returns a [Result] containing either the result or a custom error.
 *
 * If the block throws an exception, the exception is transformed into a custom error using `toCustomError()` before being wrapped in a failure result.
 *
 * @return A [Result] containing the block's result on success, or a custom error on failure.
 */
inline fun <T, R> T.runHandledCatching(block: T.() -> R): Result<R> {
    return runSuspendCatching(block).fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toCustomError()) },
    )
}
