package ru.nobird.app.kmm_test.android.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ru.nobird.app.core.model.Cancellable

fun Lifecycle.addCancellable(cancellableFactory: () -> Cancellable): LifecycleCancelable =
    LifecycleCancelable(this, cancellableFactory)

class LifecycleCancelable(
    private val lifecycle: Lifecycle,
    private val cancellableFactory: () -> Cancellable
) : LifecycleEventObserver, Cancellable {
    private var cancellable: Cancellable? = null

    init {
        lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START ->
                cancellable = cancellableFactory()

            Lifecycle.Event.ON_STOP -> {
                cancellable?.cancel()
                cancellable = null
            }

            Lifecycle.Event.ON_DESTROY ->
                cancel()

            else -> {
                // no op
            }
        }
    }

    override fun cancel() {
        lifecycle.removeObserver(this)

        cancellable?.cancel()
        cancellable = null
    }
}

@Composable
fun LoadingState() {
    CircularProgressIndicator()
}

@Composable
fun ErrorState() {
    Text(text = "Network error")
}


fun flatTopBar(
    title: String,
    navigationClick: UnitListener? = null,
) = @Composable {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationClick?.let { navigationIcon(onClick = navigationClick) },
    )
}

@Composable
fun navigationIcon(onClick: () -> Unit): @Composable UnitListener = {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Navigation"
        )
    }
}

typealias UnitListener = (() -> Unit)
