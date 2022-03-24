package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.kmm_test.users.detail.UserDetailsDispatcher
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature
import ru.nobird.app.kmm_test.users.detail.UserDetailsReducer
import ru.nobird.app.kmm_test.users.list.UsersListDispatcher
import ru.nobird.app.kmm_test.users.list.UsersListFeature
import ru.nobird.app.kmm_test.users.list.UsersListReducer
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher

object ApplicationFeatureBuilder {
    private val actionDispatcherOptions = ActionDispatcherOptions()
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(
            State(
                backStack = emptyList(),
                graph = generateScreenGraph(),
                screen = State.ScreenState.UserListScreen(UsersListFeature.State.Idle())
            ),
            ApplicationReducer(
                UsersListReducer(),
                UserDetailsReducer()
            )
        )
            .wrapWithActionDispatcher(ApplicationDispatcher())
            .wrapWithActionDispatcher(UsersListDispatcher(actionDispatcherOptions).transform(
                transformAction = { it.safeCast<Action.UserListAction>()?.action },
                transformMessage = Message::UserListMessage
            ))
            .wrapWithActionDispatcher(UserDetailsDispatcher(actionDispatcherOptions).transform(
                transformAction = { it.safeCast<Action.UserDetailsAction>()?.action },
                transformMessage = Message::UserDetailsMessage
            ))


    private fun generateScreenGraph(): NavGraph {
        val nameTriples = SCREEN_NAMES.shuffled().zipWithNext().zip(SCREEN_NAMES)
        return nameTriples.associate { (shuffled, name3) ->
            val (name1, name2) = shuffled
            name1 to (name2 to name3)
        }
    }

    private val SCREEN_NAMES =
        listOf(
            "UsersListFeature",
            "UserDetailsFeature",
        )
}