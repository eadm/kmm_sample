package ru.nobird.app.kmm_test.android.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import ru.nobird.app.kmm_test.android.ui.users.detail.UserDetailsComposable
import ru.nobird.app.kmm_test.android.ui.users.list.MainScreen
import ru.nobird.app.kmm_test.aplication.ApplicationFeature
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeatureBuilder
import ru.nobird.app.kmm_test.users.list.UsersListFeatureBuilder
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

@Composable
fun KMMAppSample(
    state: State,
    message: (Message) -> Unit
) {
    when (val screen = state.currentScreen) {
        is State.ScreenState.UserListScreen ->
            MainScreen(
                state = screen.state,
                message = {
                    message(Message.UserListMessage(it))
                }
            )
        is State.ScreenState.UserDetailsScreen ->
            UserDetailsComposable(
                userName = "eadm",
                state = screen.state,
                message = {
                    message(Message.UserDetailsMessage(it))
                },
                onBackClicked = {
                    message(Message.BackPressed)
                },
            )
    }
}