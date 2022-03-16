package ru.nobird.app.kmm_test.android.ui

import androidx.compose.runtime.*
import ru.nobird.app.kmm_test.android.ui.users.detail.UserDetailsComposable
import ru.nobird.app.kmm_test.android.ui.users.list.UsersListComposable
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State

@Composable
fun KMMAppSample(
    state: State,
    message: (Message) -> Unit
) {
    when (val screen = state.currentScreen) {
        is State.ScreenState.UserListScreen ->
            UsersListComposable(
                state = screen.state,
                message = {
                    message(Message.UserListMessage(it))
                },
                navigate = {
                    message(Message.OnUserDetailsScreenSwitch)
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