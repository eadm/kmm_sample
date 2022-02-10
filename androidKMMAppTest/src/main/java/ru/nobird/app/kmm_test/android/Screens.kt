package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object MainScreen : Screen {

    @Composable
    override fun Content() {
        MainContent()
    }
}

data class DetailsScreen(val user: UiUser) : Screen {

    @Composable
    override fun Content() {
        DetailsContent(user = user)
    }
}
