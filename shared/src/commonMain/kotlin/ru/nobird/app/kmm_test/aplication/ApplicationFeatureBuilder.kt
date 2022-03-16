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
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(
            State(
                screens = listOf(
                    State.ScreenState.UserListScreen(UsersListFeature.State.Idle),
                    State.ScreenState.UserDetailsScreen(UserDetailsFeature.State.Idle)
                ),
                currentScreenPos = 0
            ),
            ApplicationReducer(
                UsersListReducer(),
                UserDetailsReducer()
            )
        )
            .wrapWithActionDispatcher(ApplicationDispatcher())
            .wrapWithActionDispatcher(UsersListDispatcher(ActionDispatcherOptions()).transform(
                transformAction = { it.safeCast<Action.UserListAction>()?.action },
                transformMessage = Message::UserListMessage
            ))
            .wrapWithActionDispatcher(UserDetailsDispatcher(ActionDispatcherOptions()).transform(
                transformAction = { it.safeCast<Action.UserDetailsAction>()?.action },
                transformMessage = Message::UserDetailsMessage
            ))
}