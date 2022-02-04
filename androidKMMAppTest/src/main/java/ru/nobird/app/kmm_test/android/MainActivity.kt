package ru.nobird.app.kmm_test.android

import android.os.Bundle
import android.text.util.Linkify
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.kmm_test.android.databinding.ActivityMainBinding
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.data.model.UsersQuery
import ru.nobird.app.kmm_test.user.UserFeature
import ru.nobird.app.kmm_test.user.UserFeatureBuilder
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val usersAdapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersListFeature = UsersListFeatureBuilder.build()

        val userFeature = UserFeatureBuilder.build()

        setContent {
            val navHostController = rememberNavController()

            AppNavHost(
                navHostController = navHostController,
                usersListFeature = usersListFeature,
                userFeature = userFeature
            )
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

enum class Screens {
    HOME, DETAILS
}

@Composable
private fun AppNavHost(
    navHostController: NavHostController,
    usersListFeature: Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>,
    userFeature: Feature<UserFeature.State, UserFeature.Message, UserFeature.Action>
) {
    NavHost(navController = navHostController, startDestination = Screens.HOME.name) {
        composable(Screens.HOME.name) {
            MainScreen(usersListFeature = usersListFeature, onItemClick = {
                navHostController.navigate("${Screens.DETAILS.name}/${it.login}")
            })
        }

        composable("${Screens.DETAILS.name}/{url}", arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            }
        )) { entry ->
            val userUrl = entry.arguments?.getString("url")!!
            DetailsScreen(userFeature)
            userFeature.onNewMessage(
                message = UserFeature.Message.Init(
                    forceUpdate = true,
                    userUrl = userUrl
                )
            )

        }

    }
}

@Composable
private fun DetailsScreen(
    userFeature: Feature<UserFeature.State, UserFeature.Message, UserFeature.Action>
) {
    var featureState by remember {
        mutableStateOf(userFeature.state)
    }
    LocalLifecycleOwner.current.lifecycle.addCancellable {
        userFeature.addStateListener { featureState = it }
        userFeature
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            when (val state = featureState) {
                is UserFeature.State.Idle, is UserFeature.State.Loading -> LoadingState()
                is UserFeature.State.NetworkError -> ErrorState()
                is UserFeature.State.Data -> UserDataState(user = state.user)
            }
        }
    }
}

@Composable
private fun UserDataState(user: User) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = user.login)
            })
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(data = user.avatarUrl),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                )
            }
        }
    )
}


@Composable
private fun MainScreen(
    usersListFeature: Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>,
    onItemClick: (User) -> Unit
) {
    var queryText by remember { mutableStateOf("test") }
    var featureState by remember { mutableStateOf(usersListFeature.state) }

    val focusManager = LocalFocusManager.current

    LocalLifecycleOwner.current.lifecycle
        .addCancellable {
            usersListFeature.addStateListener { featureState = it }
            usersListFeature
        }

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
                    DataState(state = state, onItemClick) {
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
fun UserData(user: User, onItemClick: (User) -> Unit) {
    Row(
        modifier = Modifier
            .clickable {
                onItemClick.invoke(user)
            }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = user.avatarUrl),
            contentDescription = user.avatarUrl,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = user.login, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Type: ${user.type}", style = MaterialTheme.typography.subtitle2)
        }
    }
}

@Composable
fun DataState(
    state: UsersListFeature.State.Data,
    onItemClick: (User) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(state.users, key = { _, item -> item.id }) { index, item ->
            UserData(user = item, onItemClick)

            if (index + 3 > state.users.size) {
                SideEffect {
                    onLoadMore()
                }
            }
        }

        if (state.isLoading) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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