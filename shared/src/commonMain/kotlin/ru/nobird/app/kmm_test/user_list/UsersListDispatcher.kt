package ru.nobird.app.kmm_test.user_list

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import ru.nobird.android.presentation.redux.dispatcher.CoroutineActionDispatcher
import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.kmm_test.data.model.UsersResponse
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Action
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Message

class UsersListDispatcher(
    config: ActionDispatcherOptions,
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchUsers -> {
                val httpClient = HttpClient {
                    install(JsonFeature) {
                        val json = Json {
                            ignoreUnknownKeys = true
                        }
                        serializer = KotlinxSerializer(json)
                    }
                }
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
        }
    }
}