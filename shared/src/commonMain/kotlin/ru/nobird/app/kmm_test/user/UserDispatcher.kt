package ru.nobird.app.kmm_test.user

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.user.UserFeature.Action
import ru.nobird.app.kmm_test.user.UserFeature.Message

class UserDispatcher(
    config: ActionDispatcherOptions,
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchUser -> {
                val httpClient = HttpClient {
                    install(JsonFeature) {
                        val json = Json {
                            ignoreUnknownKeys = true
                        }
                        serializer = KotlinxSerializer(json)
                    }
                }
                try {
                    val response: User = httpClient.get("https://api.github.com/users/${action.userUrl}")
                    onNewMessage(Message.UserLoaded.Success(response))
                } catch (e: Exception) {
                    onNewMessage(
                        Message.UserLoaded.Error(
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