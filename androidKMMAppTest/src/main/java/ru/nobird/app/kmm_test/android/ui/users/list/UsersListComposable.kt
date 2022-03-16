package ru.nobird.app.kmm_test.android.ui.users.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.nobird.app.kmm_test.android.ui.ErrorState
import ru.nobird.app.kmm_test.android.ui.LoadingState
import ru.nobird.app.kmm_test.android.ui.addCancellable
import ru.nobird.app.kmm_test.data.users.list.model.UsersQuery
import ru.nobird.app.kmm_test.users.list.UsersListFeature
import ru.nobird.app.presentation.redux.feature.Feature

@Composable
fun UsersListComposable(
    state: UsersListFeature.State,
    message: (UsersListFeature.Message) -> Unit,
    navigate: () -> Unit
) {
    var queryText by rememberSaveable { mutableStateOf("test") }
//    var featureState by remember { mutableStateOf(usersListFeature.state) }

    val focusManager = LocalFocusManager.current

//    LocalLifecycleOwner.current.lifecycle
//        .addCancellable {
//            usersListFeature.addStateListener { featureState = it }
//            usersListFeature
//        }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = queryText,
            onValueChange = { queryText = it },
            label = { Text(text = "Query") },
            keyboardActions = KeyboardActions(onSearch = {
                message(
                    UsersListFeature.Message.Init(
                        forceUpdate = true,
                        UsersQuery(userName = queryText)
                    )
                )
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )

        Box(
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is UsersListFeature.State.Idle, is UsersListFeature.State.Loading ->
                    LoadingState()

                is UsersListFeature.State.Data ->
                    DataState(
                        state = state,
                        navigate = navigate,
                        onLoadMore = {
                            message(UsersListFeature.Message.LoadNextPage)
                        }
                    )

                is UsersListFeature.State.NetworkError ->
                    ErrorState()
            }
        }
    }

}

@Composable
fun DataState(
    state: UsersListFeature.State.Data,
    navigate: () -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, modifier = Modifier
        .fillMaxWidth()
        .clickable { }
    ) {
        itemsIndexed(state.users, key = { _, item -> item.id }) { index, item ->
            Text(
                text = item.login,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navigate() }
            )

            if (index + 3 > state.users.size) {
                SideEffect {
                    onLoadMore()
                }
            }
        }

        if (state.isLoading) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .progressSemantics()
                            .size(24.dp)
                    )
                }
            }
        }
    }
}
