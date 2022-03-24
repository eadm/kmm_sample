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
            is State.ScreenState.UsersListScreenState ->
                when (message) {
                    is Message.OnGoToClicked -> goToReducer(message.screen, state)
                    Message.BackPressed -> goBackReducer(state)

                    is Message.UserListMessage -> reduceUserList(
                        currentScreenState = state.screen,
                        message = message.message,
                        state = state
                    )
                    is Message.UserDetailsMessage -> null
                } ?: state to setOf()

            is State.ScreenState.UserDetailsScreenState ->
                when (message) {
                    is Message.OnGoToClicked -> goToReducer(message.screen, state)
                    Message.BackPressed -> goBackReducer(state)

                    is Message.UserDetailsMessage -> reduceUserDetail(
                        currentScreen = state.screen,
                        message = message.message,
                        state = state
                    )
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
        screenState: Screen<String>,
        state: State
    ): Pair<State, Set<Action>> =
        when (val backScreen = state.backStack.find { it == screenState }) {
            null -> {
                val newBackStack = state.backStack + state.screen
                val screen = when (screenState.screenState) {
                    is State.ScreenState.UsersListScreenState ->
                        State.ScreenState.UsersListScreenState(
                            state = UsersListFeature.State.Idle()
                        )

                    is State.ScreenState.UserDetailsScreenState ->
                        State.ScreenState.UserDetailsScreenState(
                            state = UserDetailsFeature.State.Idle,
                            userName = screenState.arg ?: ""
                        )
                }
                state.copy(backStack = newBackStack, screen = screen) to emptySet()
            }
            else -> {
                val newBackStack = state.backStack.takeWhile { it != screenState }
                state.copy(backStack = newBackStack, screen = backScreen) to emptySet()
            }
        }

    private fun reduceUserDetail(
        currentScreen: State.ScreenState.UserDetailsScreenState,
        message: UserDetailsFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userDetailsState, userDetailsAction) = userDetailsReducer.reduce(
            state = currentScreen.state,
            message = message
        )
        val newAction = userDetailsAction.map(Action::UserDetailsAction).toSet()
        return when (state.screen) {
            is State.ScreenState.UsersListScreenState ->
                null
            is State.ScreenState.UserDetailsScreenState ->
                state.copy(screen = state.screen.copy(userDetailsState)) to newAction
        } ?: state to setOf()
    }

    private fun reduceUserList(
        currentScreenState: State.ScreenState.UsersListScreenState,
        message: UsersListFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userListState, userListAction) = usersListReducer.reduce(
            state = currentScreenState.state,
            message = message
        )
        val newAction = userListAction.map(Action::UserListAction).toSet()
        return when (state.screen) {
            is State.ScreenState.UserDetailsScreenState ->
                null
            is State.ScreenState.UsersListScreenState ->
                state.copy(screen = state.screen.copy(userListState)) to newAction
        } ?: state to setOf()
    }
}
