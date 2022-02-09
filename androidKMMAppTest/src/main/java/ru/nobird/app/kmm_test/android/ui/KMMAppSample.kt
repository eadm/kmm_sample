package ru.nobird.app.kmm_test.android.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import ru.nobird.app.kmm_test.android.ui.users.detail.UserDetailsComposable
import ru.nobird.app.kmm_test.android.ui.users.list.MainScreen
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeatureBuilder
import ru.nobird.app.kmm_test.users.list.UsersListFeatureBuilder
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Feature.USERS_LIST
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Feature.USERS_DETAIL
import ru.nobird.app.presentation.redux.feature.Feature

@Composable
fun KMMAppSample(feature: Feature<State, Message, Action>) {
    var featureState by remember { mutableStateOf(feature.state) }

    LocalLifecycleOwner.current.lifecycle
        .addCancellable {
            feature.addStateListener { featureState = it }
            feature
        }

    when (val state = featureState) {
        is State.Screen ->
            when(state.stack.first()) {
                USERS_LIST -> {
                    MainScreen(usersListFeature = UsersListFeatureBuilder.build()) {
                        //click on user
                        feature.onNewMessage(Message.NavigateClick(USERS_DETAIL))
                    }
                }
                USERS_DETAIL ->
                    UserDetailsComposable(userFeature = UserDetailsFeatureBuilder.build()) {
                        //back pressed
                        feature.onNewMessage(Message.BackPressed)
                    }
            }
    }
}