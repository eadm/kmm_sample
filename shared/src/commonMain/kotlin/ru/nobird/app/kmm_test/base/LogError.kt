package ru.nobird.app.kmm_test.base

/**
 * Logs error, then throws it
 */
internal fun throwError(throwable: Throwable): Nothing {
    // you can add logging here
    throw throwable
}
