package ru.nobird.app.kmm_test.users.detail

import io.ktor.client.request.*
import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.kmm_test.data.users.detail.model.UserDetailsResponse
import ru.nobird.app.kmm_test.network.KtorClient.httpClient
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Action
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

private const val URL = "https://api.github.com/users/"

class UserDetailsDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when(action) {
            is Action.FetchUserDetails -> {
                try {
                    val response: UserDetailsResponse = httpClient.get(URL + action.userName)
                    onNewMessage(Message.UserDetailsLoaded.Success(response))
                } catch (e: Exception) {
                    onNewMessage(
                        Message.UserDetailsLoaded.Error(
                            error = e.message ?: ""
                        )
                    )
                }
            }
            is Action.ViewAction -> {
                // no op
            }
        }
    }
}