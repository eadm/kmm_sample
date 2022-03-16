package ru.nobird.app.kmm_test.users.list

import ru.nobird.app.kmm_test.data.users.list.model.User
import ru.nobird.app.kmm_test.data.users.list.model.UsersQuery

interface UsersListFeature {
    sealed interface State {

        val inputData: InputData

        data class Idle(
            override val inputData: InputData = InputData(userName = "")
        ) : State

        data class Loading(override val inputData: InputData) : State

        data class Data(
            override val inputData: InputData,
            val users: List<User>,
            val isLoading: Boolean
        ) : State

        object NetworkError : State {
            override val inputData: InputData
                get() = InputData(userName = "")
        }

        data class InputData(val userName: String)
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

        data class OnUserNameInput(
            val userName: String
        ) : Message

        object LoadNextPage : Message
    }

    sealed interface Action {
        data class FetchUsers(val usersQuery: UsersQuery) : Action
        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction
        }
    }
}