package ru.nobird.app.kmm_test.android

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
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


@ExperimentalSerializationApi
class NavViewModelFactory<S : Any>(
    owner: SavedStateRegistryOwner,
    private val serializer: KSerializer<S>
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T =
        NavViewModel(handle, serializer) as T
}