package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.presentation.redux.feature.Feature

data class MainScreen(val usersListFeature: Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>) : Screen {

    @Composable
    override fun Content() {
        MainContent(usersListFeature = usersListFeature)
    }
}

data class DetailsScreen(val user: User) : Screen {

    @Composable
    override fun Content() {
        DetailsContent(user = user)
    }
}
