package ru.nobird.app.kmm_test.android.ui

import androidx.compose.runtime.*
import ru.nobird.app.kmm_test.android.ui.users.detail.UserDetailsComposable
import ru.nobird.app.kmm_test.android.ui.users.list.UsersListComposable
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.kmm_test.aplication.Screen

@Composable
fun KMMAppSample(
    state: State,
    message: (Message) -> Unit
) {
    when (val screen = state.screen) {
        is State.ScreenState.UsersListScreenState ->
            UsersListComposable(
                state = screen.state,
                message = {
                    message(Message.UserListMessage(it))
                },
                navigate = {
                    message(Message.OnGoToClicked(screen = it))
                }
            )
        is State.ScreenState.UserDetailsScreenState ->
            UserDetailsComposable(
                userName = screen.userName,
                state = screen.state,
                message = {
                    message(Message.UserDetailsMessage(it))
                },
                onBackClicked = {
                    message(Message.BackPressed)
                }
            )
    }
}