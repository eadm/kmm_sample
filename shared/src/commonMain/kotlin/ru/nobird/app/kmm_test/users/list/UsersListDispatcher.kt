package ru.nobird.app.kmm_test.users.list

import io.ktor.client.request.*
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.kmm_test.data.users.list.model.UsersResponse
import ru.nobird.app.kmm_test.network.KtorClient.httpClient
import ru.nobird.app.kmm_test.users.list.UsersListFeature.Action
import ru.nobird.app.kmm_test.users.list.UsersListFeature.Message

class UsersListDispatcher(
    config: ActionDispatcherOptions,
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchUsers -> {
                try {
                    val response: UsersResponse = httpClient.get("https://api.github.com/search/users?q=${action.usersQuery.userName}&page=1&per_page=20")
                    onNewMessage(Message.UsersLoaded.Success(response.items))
                } catch (e: Exception) {
                    onNewMessage(
                        Message.UsersLoaded.Error(
                            errorMsg = e.message ?: ""
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