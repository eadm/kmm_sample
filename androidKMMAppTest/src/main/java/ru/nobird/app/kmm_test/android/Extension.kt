package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.*
import ru.nobird.app.presentation.redux.feature.Feature


@Composable
fun <S, M, A> Feature<S, M, A>.observeState(): State<S> {
    val mutableState = remember { mutableStateOf(this.state) }
    DisposableEffect(this) {
        val cancelable = addStateListener {
            mutableState.value = it
        }
        onDispose {
            cancelable.cancel()
        }
    }
    return mutableState
}


fun <T: Any> NavigationModel<T>.onNavigate(screen: T) {
    this.push(screen)
}