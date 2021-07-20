package ru.nobird.app.kmm_test.base

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import ru.nobird.android.presentation.redux.dispatcher.CoroutineActionDispatcher

/**
 * Null scope means ActionDispatcher can decide for itself
 * (default behavior: MainDispatcher() will be used for both actionParentScope and messageParentScope)
 */
class ActionDispatcherOptions(
    override val actionParentScope: CoroutineScope? = null,
    override val messageParentScope: CoroutineScope? = null
) : CoroutineActionDispatcher.ScopeConfigOptions {

    override val actionScopeExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) {
            throwError(throwable) // rethrow if not cancellation exception
        }
    }

    override val messageScopeExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwError(throwable) // rethrow
    }
}