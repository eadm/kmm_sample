package ru.nobird.app.kmm_test.user_list

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Action
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Message
import ru.nobird.app.kmm_test.user_list.UsersListFeature.State

class UserListReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchUsers(message.usersQuery))
                } else {
                    null
                }

            Message.LoadNextPage ->
                when (state) {
                    is State.Data -> null
//                        if (state.users.hasNext && !state.isLoading) {
//                            val nextPage = state.users.page + 1
//                            state.copy(isLoading = true) to setOf(
//                                Action.FetchUsers(
//                                    UsersQuery(
//                                        page = nextPage
//                                    )
//                                )
//                            )
//                        } else null
                    else -> null
                }

            is Message.UsersLoaded.Success ->
                if (state is State.Data) {
                    val users = state.users.plus(message.users)
                    state.copy(users = users, isLoading = false) to emptySet()
                } else {
                    State.Data(users = message.users, isLoading = false) to emptySet()
                }

            is Message.UsersLoaded.Error ->
                when (state) {
                    is State.Loading -> State.NetworkError to setOf()
                    is State.Data -> state.copy(isLoading = false) to setOf(Action.ViewAction.ShowNetworkError)
                    else -> null
                }

        } ?: state to emptySet()
}
