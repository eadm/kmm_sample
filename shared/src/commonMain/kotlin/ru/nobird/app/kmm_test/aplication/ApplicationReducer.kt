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
        when (state.currentScreen) {
            is State.ScreenState.UserListScreen ->
                when (message) {
                    is Message.UserListMessage ->
                        reduceUserList(state.currentScreen, message.message, state)

                    Message.OnUserDetailsScreenSwitch ->
                        state.copy(currentScreenPos = 1) to setOf()

                    Message.BackPressed ->
                        state to setOf(Action.Finish)

                    else -> state to setOf()
                }

            is State.ScreenState.UserDetailsScreen ->
                when (message) {
                    is Message.UserDetailsMessage ->
                        reduceUserDetail(state.currentScreen, message.message, state)

                    Message.OnUserListScreenSwitch ->
                        state.copy(currentScreenPos = 0) to setOf()

                    Message.BackPressed ->
                        state to setOf(Action.Finish)

                    else -> state to setOf()
                }
        }

    private fun reduceUserDetail(
        currentScreen: State.ScreenState.UserDetailsScreen,
        message: UserDetailsFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userDetailsState, userDetailsAction) = userDetailsReducer.reduce(currentScreen.state, message)
        val newAction = userDetailsAction.map(Action::UserDetailsAction).toSet()
        return state.changeCurrentScreen<State.ScreenState.UserDetailsScreen> {
            copy(state = userDetailsState)
        } to newAction
    }

    private fun reduceUserList(
        currentScreen: State.ScreenState.UserListScreen,
        message: UsersListFeature.Message,
        state: State
    ): Pair<State, Set<Action>> {
        val (userListState, userListAction) = usersListReducer.reduce(currentScreen.state, message)
        val newAction = userListAction.map(Action::UserListAction).toSet()
        return state.changeCurrentScreen<State.ScreenState.UserListScreen> {
            copy(state = userListState)
        } to newAction
    }
}