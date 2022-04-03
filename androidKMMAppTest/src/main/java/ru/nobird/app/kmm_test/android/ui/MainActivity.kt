package ru.nobird.app.kmm_test.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import ru.nobird.app.kmm_test.android.App
import ru.nobird.app.presentation.redux.feature.Feature

class MainActivity : AppCompatActivity() {
    private val feature
        get() = (application as App).feature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            feature.Application { state, listener ->
                KMMAppSample(state, listener)
            }
        }
    }
}

@Composable
fun <State : Any, Message : Any> Feature<State, Message, *>.Application(
    composable: @Composable (state: State, message: (Message) -> Unit) -> Unit
) {
    composable(
        asComposeState().value, ::onNewMessage
    )
}

@Composable
fun <T : Any> Feature<T, *, *>.asComposeState(): State<T> {
    val state = currentComposer.cache(false) { mutableStateOf(state) }
//    val state = rememberSaveable { mutableStateOf(state) }
    DisposableEffect(this) {
        addStateListener { state.value = it }
        onDispose {
//            cancel()
        }
    }
    return state
}
