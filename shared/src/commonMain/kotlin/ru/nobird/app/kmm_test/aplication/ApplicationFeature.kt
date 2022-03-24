package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.kmm_test.users.list.UsersListFeature

typealias NavGraph = Map<String, Pair<String, String>>

sealed interface ApplicationFeature {

    data class State(
        val backStack: List<ScreenState>,
        val graph: NavGraph,
        val screen: ScreenState
    ) {
        sealed class ScreenState {
            abstract var name: String

            data class UserListScreen(
                val state: UsersListFeature.State,
                override var name: String = "UserListScreen"
            ) : ScreenState()

            data class UserDetailsScreen(
                val state: UserDetailsFeature.State,
                override var name: String = "UserDetailsScreen"
            ) : ScreenState()
        }
    }

    sealed interface Message {
        data class OnGoToClicked(val screenName: String) : Message

        data class UserListMessage(val message: UsersListFeature.Message) : Message
        data class UserDetailsMessage(val message: UserDetailsFeature.Message) : Message

        object BackPressed : Message
    }

    sealed interface Action {
        data class UserListAction(val action: UsersListFeature.Action) : Action
        data class UserDetailsAction(val action: UserDetailsFeature.Action) : Action
        object Finish : Action
    }
}