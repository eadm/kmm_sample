package ru.nobird.app.kmm_test.android

import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import ru.nobird.app.kmm_test.data.model.User

object Screens {
    val mainScreen = Screen(
        key = "main",
        onCreate = { _, arg ->
            val router = arg.get<TreeRouter>()
            return@Screen MainViewModel(router)
        },
        content = {
            MainScreen(viewModel = it.get<MainViewModel>())
        }
    )

    val detailsScreen = Screen<User>(
        key = "details",
        onCreate = { _, arg ->
            return@Screen arg.get<User>()
        },
        content = {
            DetailsContent(userDetails = it.get())
        }
    )
}