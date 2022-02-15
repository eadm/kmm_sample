package ru.nobird.app.kmm_test.user

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.user.UserFeature.Action
import ru.nobird.app.kmm_test.user.UserFeature.Message
import ru.nobird.app.kmm_test.user.UserFeature.State

class UserReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchUser(message.userUrl))
                } else {
                    null
                }

            is Message.UserLoaded.Success ->
                State.Data(user = message.user, isLoading = false) to emptySet()

            is Message.UserLoaded.Error ->
                when (state) {
                    is State.Loading -> State.NetworkError to setOf()
                    is State.Data -> state.copy(isLoading = false) to setOf(Action.ViewAction.ShowNetworkError)
                    else -> null
                }

        } ?: state to emptySet()
}
