package ru.nobird.app.kmm_test.android

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.data.model.UsersQuery
import ru.nobird.app.kmm_test.user_list.UsersListFeature

@ExperimentalSerializationApi
class MainActivity : ComponentActivity() {
    private val viewModel: NavigationModel<Screen> by viewModels {
        NavViewModelFactory(this, Screen.serializer())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent(Screen.Main, viewModel)
        }
    }
}

@Composable
fun Details(user: User, openUrl: (Screen.Web) -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "User details")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            IconButton(
                onClick = { openUrl.invoke(Screen.Web(user.htmlUrl)) },
                Modifier.background(MaterialTheme.colors.secondary, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Repositories"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colors.secondary, CircleShape),
                model = user.avatarUrl,
                contentDescription = null
            )
            Text(text = user.login)
        }
    }
}

@ExperimentalSerializationApi
@Composable
fun MainContent(startScreen: Screen, navigationModel: NavigationModel<Screen>) {
    val onNavigate: (Screen) -> Unit = {
        navigationModel.push(it)
    }
    if (navigationModel.isEmpty) {
        LaunchedEffect(navigationModel) {
            coroutineScope {
                onNavigate(startScreen)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (val screen = navigationModel.current) {
            is Screen.Main -> MainScreen {
                onNavigate(it)
            }
            is Screen.Details -> Details(
                screen.user,
                openUrl = { onNavigate(it) },
                onBack = { navigationModel.pop() }
            )
            is Screen.Web -> {
                WebViewPage(url = screen.url)
            }
            else -> {}
        }
    }
    BackHandler(navigationModel.backEnabled()) {
        navigationModel.pop()
    }
}


@Composable
private fun MainScreen(onDetails: (Screen.Details) -> Unit) {
    val mainViewModel: MainViewModel = viewModel()
    val usersListFeature = mainViewModel.feature
    var queryText by mainViewModel.query
    val featureState by usersListFeature.observeState()

    val focusManager = LocalFocusManager.current

    LocalLifecycleOwner.current.lifecycle
        .addCancellable {
            usersListFeature.addActionListener { action ->
                if (action is UsersListFeature.Action.ViewAction) {
//                    when (action) {
//                        is UsersListFeature.Action.ViewAction.ShowNetworkError
//                    }
                }
            }
        }

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
                    DataState(state = state, onDetails) {
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
fun DataState(
    state: UsersListFeature.State.Data,
    onDetails: (Screen.Details) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        itemsIndexed(state.users, key = { _, item -> item.id }) { index, item ->
            ListItem(
                user = item,
                onDetails
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

object TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawTrianglePath(size)
        )
    }

}

fun drawTrianglePath(size: Size): Path {
    return Path().apply {
        moveTo(size.width, 0f)
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }
}

@Composable
fun ListItem(user: User, onDetails: (Screen.Details) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 2.dp,
        shape = CutCornerShape(8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color(232, 236, 248),
                    ),
                    start = Offset(Float.POSITIVE_INFINITY, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ), TriangleShape
            )
            .clickable { onDetails.invoke(Screen.Details(user)) }
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user
                    .avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CutCornerShape(8.dp))
                    .border(2.dp, MaterialTheme.colors.secondary, CutCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = user.login)
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

@Composable
fun WebViewPage(url: String) {
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    backEnabled = view.canGoBack()
                }
            }
            loadUrl(url)
            webView = this
        }
    }, update = {
        webView = it
    }
    )
    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}