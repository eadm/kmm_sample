package ru.nobird.app.kmm_test.user_list

import ru.nobird.app.core.model.PagedList
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.data.model.UsersQuery

interface UsersListFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Data(
            val users: List<User>,
            val isLoading: Boolean
        ) : State

        object NetworkError : State
    }

    sealed interface Message {
        data class Init(
            val forceUpdate: Boolean = false,
            val usersQuery: UsersQuery = UsersQuery()
        ) : Message

        sealed interface UsersLoaded : Message {
            data class Success(
                val users: List<User>
            ) : UsersLoaded

            data class Error(
                val errorMsg: String
            ) : UsersLoaded
        }

        object LoadNextPage : Message
    }

    sealed interface Action {
        data class FetchUsers(val usersQuery: UsersQuery) : Action
        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction
        }
    }
}