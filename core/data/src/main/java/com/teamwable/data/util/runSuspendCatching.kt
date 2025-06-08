package com.teamwable.data.util

import com.teamwable.network.util.toCustomError
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes a block of code on the receiver and returns its result wrapped in a [Result], capturing exceptions.
 *
 * If the block completes successfully, returns [Result.success] with the result.
 * If a [TimeoutCancellationException] or any other [Throwable] (except [CancellationException]) is thrown, returns [Result.failure] with the exception.
 * If a [CancellationException] is thrown, it is rethrown and not wrapped.
 *
 * @return [Result] containing the result of the block or the exception thrown.
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
 * Executes a block of code on the receiver and returns a [Result] with custom error handling.
 *
 * Runs the given block, capturing any exceptions. If an exception occurs, it is transformed into a custom error using `toCustomError()` before being wrapped in a failure result.
 *
 * @return A [Result] containing the block's return value on success, or a custom error on failure.
 */
inline fun <T, R> T.runHandledCatching(block: T.() -> R): Result<R> {
    return runSuspendCatching(block).fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toCustomError()) },
    )
}
