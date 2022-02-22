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
fun KMMAppSample(feature: Feature<State, Message, Action>) {
    var featureState by remember { mutableStateOf(feature.state) }

    LocalLifecycleOwner.current.lifecycle
        .addCancellable {
            feature.addStateListener { featureState = it }
            feature
        }

    when (val state = featureState) {
        is State.Screen ->
            when(state.feature) {
                ApplicationFeature.Feature.UserList -> {
                    MainScreen(usersListFeature = UsersListFeatureBuilder.build()) { userName ->
                        //click on user
                        feature.onNewMessage(Message.Navigate(ApplicationFeature.Feature.UserDetails(userName)))
                    }
                }
                is ApplicationFeature.Feature.UserDetails ->
                    UserDetailsComposable(userName = (state.feature as ApplicationFeature.Feature.UserDetails).data, userFeature = UserDetailsFeatureBuilder.build()) {
                        //back pressed
                        feature.onNewMessage(Message.BackPressed)
                    }
            }
    }
}