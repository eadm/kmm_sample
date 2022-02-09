package ru.nobird.app.kmm_test.android.ui.users.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import ru.nobird.app.kmm_test.android.ui.ErrorState
import ru.nobird.app.kmm_test.android.ui.LoadingState
import ru.nobird.app.kmm_test.android.ui.addCancellable
import ru.nobird.app.kmm_test.android.ui.flatTopBar
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.presentation.redux.feature.Feature

@Composable
fun UserDetailsComposable(
    userFeature: Feature<
        UserDetailsFeature.State,
        UserDetailsFeature.Message,
        UserDetailsFeature.Action>,
    onBackClicked: () -> Unit) {

    var featureState by remember { mutableStateOf(userFeature.state) }

    LocalLifecycleOwner.current.lifecycle
        .addCancellable {
            userFeature.addStateListener { featureState = it }
            userFeature
        }

    userFeature.onNewMessage(
        UserDetailsFeature.Message.Init(forceUpdate = true, "eadm")
    )
    Scaffold(topBar = flatTopBar(title = "User Detail", navigationClick = onBackClicked)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                when (val state = featureState) {
                    is UserDetailsFeature.State.Idle, is UserDetailsFeature.State.Loading ->
                        LoadingState()

                    is UserDetailsFeature.State.Data ->
                        Text(text = state.userDetails.login ?: "Unknown")

                    is UserDetailsFeature.State.NetworkError ->
                        ErrorState()
                }
            }
        }
    }
}