package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.kmm_test.users.detail.UserDetailsReducer
import ru.nobird.app.kmm_test.users.list.UsersListFeature
import ru.nobird.app.kmm_test.users.list.UsersListReducer

class ApplicationReducer(
    private val usersListReducer: UsersListReducer,
    private val userDetailsReducer: UserDetailsReducer
) : StateReducer<State, Message, Action> {

    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (state.screen) {
            is State.ScreenState.UserListScreen ->
                when (message) {
                    is Message.OnGoToClicked -> goToReducer(message.screenName, state)
                    Message.BackPressed -> goBackReducer(state)

                    is Message.UserListMessage -> reduceUserList(state.screen, message.message, state)
                    is Message.UserDetailsMessage -> null
                } ?: state to setOf()

            is State.ScreenState.UserDetailsScreen ->
                when (message) {
                    is Message.OnGoToClicked -> goToReducer(message.screenName, state)
                    Message.BackPressed -> goBackReducer(state)

                    is Message.UserDetailsMessage -> reduceUserDetail(state.screen, message.message, state)
                    is Message.UserListMessage -> null
                } ?: state to setOf()
        }

    private fun goBackReducer(state: State): Pair<State, Set<Action>> =
        when (state.backStack.size) {
            0 -> state to setOf(Action.Finish)
            else -> {
                val newBackStack = state.backStack.dropLast(1)
                state.copy(backStack = newBackStack, screen = state.backStack.last()) to emptySet()
            }
        }

    private fun goToReducer(
        screenName: String,
        state: State
    ): Pair<State, Set<Action>> =
        when (val backScreen = state.backStack.find { it.name == screenName }) {
            null -> {
                val newBackStack = state.backStack + state.screen
                val screen = when (screenName) {
                    "UserListScreen" -> State.ScreenState.UserListScreen(UsersListFeature.State.Idle())
                    "UserDetailsScreen" -> State.ScreenState.UserDetailsScreen(UserDetailsFeature.State.Idle)
                    else -> State.ScreenState.UserListScreen(UsersListFeature.State.Idle())
                }
                state.copy(backStack = newBackStack, screen = screen) to emptySet()
            }
            else -> {
                val newBackStack = state.backStack.takeWhile { it.name != screenName }
                state.copy(backStack = newBackStack, screen = backScreen) to emptySet()
            }
        }

    private fun reduceUserDetail(
        currentScreen: State.ScreenState.UserDetailsScreen,
        message: UserDetailsFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userDetailsState, userDetailsAction) = userDetailsReducer.reduce(currentScreen.state, message)
        val newAction = userDetailsAction.map(Action::UserDetailsAction).toSet()
        return when (state.screen) {
            is State.ScreenState.UserListScreen ->
                null
            is State.ScreenState.UserDetailsScreen ->
                state.copy(screen = state.screen.copy(userDetailsState)) to newAction
        } ?: state to setOf()
    }

    private fun reduceUserList(
        currentScreen: State.ScreenState.UserListScreen,
        message: UsersListFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userListState, userListAction) = usersListReducer.reduce(currentScreen.state, message)
        val newAction = userListAction.map(Action::UserListAction).toSet()
        return when (state.screen) {
            is State.ScreenState.UserDetailsScreen ->
                null
            is State.ScreenState.UserListScreen ->
                state.copy(screen = state.screen.copy(userListState)) to newAction
        } ?: state to setOf()
    }
}
