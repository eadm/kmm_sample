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

@Composable
fun UserDetailsComposable(
    userName: String,
    state: UserDetailsFeature.State,
    message: (UserDetailsFeature.Message) -> Unit,
    onBackClicked: () -> Unit
) {

    BackPressHandler(onBackPressed = onBackClicked)

//    var featureState by remember { mutableStateOf(userFeature.state) }

//    LocalLifecycleOwner.current.lifecycle
//        .addCancellable {
//            userFeature.addStateListener { featureState = it }
//            userFeature
//        }

    message(
        UserDetailsFeature.Message.Init(
            forceUpdate = true,
            userName = userName
        )
    )
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
                    is UserDetailsFeature.State.Idle, is UserDetailsFeature.State.Loading ->
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
