package ru.nobird.app.kmm_test.users.detail

import ru.nobird.app.kmm_test.data.users.detail.model.UserDetailsResponse

interface UserDetailsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Data(
            val userDetails: UserDetailsResponse,
            val isLoading : Boolean
        ) : State

        object NetworkError : State
    }

    sealed interface Message {
        data class Init(
            val forceUpdate : Boolean,
            val userName: String
        ) : Message

        sealed interface UserDetailsLoaded : Message {
            data class Success(
                val userDetails: UserDetailsResponse
            ) : UserDetailsLoaded

            data class Error(
                val error: String
            ) : UserDetailsLoaded
        }
    }

    sealed interface Action {
        data class FetchUserDetails(
            val userName: String
        ) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction
        }
    }
}