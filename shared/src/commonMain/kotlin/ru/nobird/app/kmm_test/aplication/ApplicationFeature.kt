package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.kmm_test.users.list.UsersListFeature

sealed interface ApplicationFeature {
    data class State(
        val screens: List<ScreenState>,
        val currentScreenPos: Int
    ) {
        val currentScreen = screens[currentScreenPos]
        fun <T : ScreenState> changeCurrentScreen(block: T.() -> T): State {
            @Suppress("UNCHECKED_CAST") val newScreen = (currentScreen as? T)?.block()
            val newList = if (newScreen != null)
                screens.toMutableList().also { mutableScreens ->
                    mutableScreens[currentScreenPos] = newScreen
                } else screens
            return copy(screens = newList)
        }

        sealed class ScreenState {
            data class UserListScreen(
                val state: UsersListFeature.State
            ) : ScreenState()

            data class UserDetailsScreen(
                val state: UserDetailsFeature.State
            ) : ScreenState()
        }
    }

    sealed interface Message {
        data class UserListMessage(val message: UsersListFeature.Message) : Message
        data class UserDetailsMessage(val message: UserDetailsFeature.Message) : Message

        object OnUserListScreenSwitch : Message
        object OnUserDetailsScreenSwitch : Message

        object BackPressed : Message
    }

    sealed interface Action {
        data class UserListAction(val action: UsersListFeature.Action) : Action
        data class UserDetailsAction(val action: UserDetailsFeature.Action) : Action
        object Finish : Action
    }
}