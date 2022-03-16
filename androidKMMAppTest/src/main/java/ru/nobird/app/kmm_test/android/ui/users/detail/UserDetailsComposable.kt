package ru.nobird.app.kmm_test.android.ui.users.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import ru.nobird.app.kmm_test.android.ui.*
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.presentation.redux.feature.Feature

object Counter { var count = 0 }

@Composable
fun UserDetailsComposable(
    userName: String,
    state: UserDetailsFeature.State,
    message: (UserDetailsFeature.Message) -> Unit,
    onBackClicked: () -> Unit
) {

    BackPressHandler(onBackPressed = onBackClicked)
    Counter.count = 1
    Scaffold(
        topBar = flatTopBar(
            title = "User Detail",
            navigationClick = onBackClicked
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is UserDetailsFeature.State.Idle ->
                        message(
                            UserDetailsFeature.Message.Init(
                                forceUpdate = true,
                                userName = userName
                            )
                        )

                    is UserDetailsFeature.State.Loading ->
                        LoadingState()

                    is UserDetailsFeature.State.Data ->
                        with(state.userDetails) {
                            Text(text =
                                "Login: $login \n" +
                                "Email: $email \n" +
                                "Name:  $name"
                            )
                        }

                    is UserDetailsFeature.State.NetworkError ->
                        ErrorState()
                }
            }
        }
    }
}
