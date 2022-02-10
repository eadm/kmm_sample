package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberImagePainter
import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.kmm_test.android.databinding.ActivityMainBinding
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.data.model.UsersQuery
import ru.nobird.app.kmm_test.user_list.UsersListFeature

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val usersAdapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(screen = MainScreen)
        }

//        viewBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
//
//        usersListFeature.addStateListener(this::setState)
//
//        viewBinding.button.setOnClickListener {
//            usersListFeature.onNewMessage(
//                UsersListFeature.Message.Init(
//                    forceUpdate = true,
//                    usersQuery = UsersQuery(
//                        userName = viewBinding.userName.text.toString()
//                    )
//                )
//            )
//        }
//
//        setState(usersListFeature.state)
//        viewBinding.usersList.adapter = usersAdapter
    }

    private fun setState(state: UsersListFeature.State) {
        // TODO: 7/21/21 change to paged list
        usersAdapter.updateList((state as? UsersListFeature.State.Data)?.users)
    }
}

@Composable
fun DetailsContent(
    user: UiUser
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp),
                painter = rememberImagePainter(user.avatarUrl),
                contentDescription = "avatar :${user.gravatarId}"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "User nickname: ${user.login}")
        }
    }
}

@Composable
fun MainContent(model: MainViewModel = viewModel()) {
    var queryText by remember { mutableStateOf("test") }

    val focusManager = LocalFocusManager.current

    val usersListFeature = model.feature.value!!

    val featureState by usersListFeature.observeState()

//    LocalLifecycleOwner.current.lifecycle
//        .addCancellable {
//            usersListFeature.addActionListener { action ->
//                if (action is UsersListFeature.Action.ViewAction) {
//                    when (action) {
//                        is UsersListFeature.Action.ViewAction.ShowNetworkError
//                    }
//                }
//            }
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
                usersListFeature.onNewMessage(
                    UsersListFeature.Message.Init(
                        forceUpdate = true,
                        UsersQuery(userName = queryText)
                    )
                )
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            contentAlignment = Alignment.Center
        ) {
            when (val state = featureState) {
                is UsersListFeature.State.Idle, is UsersListFeature.State.Loading ->
                    LoadingState()

                is UsersListFeature.State.Data ->
                    DataState(state = state) {
                        usersListFeature.onNewMessage(UsersListFeature.Message.LoadNextPage)
                    }

                is UsersListFeature.State.NetworkError ->
                    ErrorState()
            }
        }
    }

}

@Composable
fun LoadingState() {
    CircularProgressIndicator()
}

@Composable
fun ErrorState() {
    Text(text = "Network error")
}

@Composable
fun UserItem(user: User, navigator: Navigator) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .clickable {
            navigator.push(
                DetailsScreen(
                    UiUser(
                        user.avatarUrl,
                        user.eventsUrl,
                        user.followersUrl,
                        user.followingUrl,
                        user.gistsUrl,
                        user.gravatarId,
                        user.htmlUrl,
                        user.id,
                        user.login,
                        user.nodeId,
                        user.organizationsUrl,
                        user.receivedEventsUrl,
                        user.reposUrl,
                        user.score,
                        user.siteAdmin,
                        user.starredUrl,
                        user.subscriptionsUrl,
                        user.type,
                        user.url,
                    )
                )
            )
        }
        .focusable(true)
        .fillMaxWidth()
        .padding(16.dp)) {
        Image(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            painter = rememberImagePainter(user.avatarUrl),
            contentDescription = "Avatar: ${user.avatarUrl}"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = user.login)
    }
}

@Composable
fun DataState(state: UsersListFeature.State.Data, onLoadMore: () -> Unit) {
    val listState = rememberLazyListState()
    val navigator = LocalNavigator.currentOrThrow
    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(state.users, key = { _, item -> item.id }) { index, item ->
            UserItem(user = item, navigator = navigator)

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


//class ComposableContainer<S, M, A>(feature: Feature<S, M, A>) {
//
//}

fun Lifecycle.addCancellable(cancellableFactory: () -> Cancellable): LifecycleCancelable =
    LifecycleCancelable(this, cancellableFactory)

class LifecycleCancelable(
    private val lifecycle: Lifecycle,
    private val cancellableFactory: () -> Cancellable
) : LifecycleEventObserver, Cancellable {
    private var cancellable: Cancellable? = null

    init {
        lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START ->
                cancellable = cancellableFactory()

            Lifecycle.Event.ON_STOP -> {
                cancellable?.cancel()
                cancellable = null
            }

            Lifecycle.Event.ON_DESTROY ->
                cancel()

            else -> {
                // no op
            }
        }
    }

    override fun cancel() {
        lifecycle.removeObserver(this)

        cancellable?.cancel()
        cancellable = null
    }
}