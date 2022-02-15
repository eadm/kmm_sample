package ru.nobird.app.kmm_test.user

import ru.nobird.app.kmm_test.data.model.User

interface UserFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Data(
            val user: User,
            val isLoading: Boolean
        ) : State

        object NetworkError : State
    }

    sealed interface Message {
        data class Init(
            val forceUpdate: Boolean = false,
            val userUrl: String = ""
        ) : Message

        sealed interface UserLoaded : Message {
            data class Success(
                val user: User
            ) : UserLoaded

            data class Error(
                val errorMsg: String
            ) : UserLoaded
        }

    }

    sealed interface Action {
        data class FetchUser(val userUrl: String) : Action
        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction
        }
    }
}