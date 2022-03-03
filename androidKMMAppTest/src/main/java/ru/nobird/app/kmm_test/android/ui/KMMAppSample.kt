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
    when (val screen = state.stack.first()) {
        ApplicationFeature.Feature.UserList -> {
            MainScreen(usersListFeature = UsersListFeatureBuilder.build()) { userName ->
                //click on user
                message(Message.Navigate(ApplicationFeature.Feature.UserDetails(userName)))
            }
        }
        is ApplicationFeature.Feature.UserDetails ->
            UserDetailsComposable(
                userName = screen.data,
                userFeature = UserDetailsFeatureBuilder.build()
            ) {
                //back pressed
                message(Message.BackPressed)
            }
    }
}