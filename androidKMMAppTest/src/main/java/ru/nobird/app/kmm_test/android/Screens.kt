package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getViewModel()
        MainContent(viewModel)
    }
}

data class DetailsScreen(val userDetails: UserDetails) : Screen {

    @Composable
    override fun Content() {
        DetailsContent(userDetails = userDetails)
    }
}
