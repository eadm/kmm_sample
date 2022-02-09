package ru.nobird.app.kmm_test.users.detail

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Action
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Message
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.State

class UserDetailsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                if (state is State.Idle ||
                    (message.forceUpdate) && (state is State.Data || state is State.NetworkError)
                ) {
                    State.Loading to setOf(Action.FetchUserDetails(message.userName))
                } else {
                    null
                }

            is Message.UserDetailsLoaded.Success ->
                if (state is State.Data) {
                    state.copy(userDetails = state.userDetails, isLoading = false) to emptySet()
                } else {
                    State.Data(userDetails = message.userDetails, isLoading = false) to emptySet()
                }

            is Message.UserDetailsLoaded.Error ->
                when (state) {
                    is State.Loading -> State.NetworkError to setOf()
                    is State.Data -> state.copy(isLoading = false) to setOf(Action.ViewAction.ShowNetworkError)
                    else -> null
                }

        } ?: state to emptySet()
}