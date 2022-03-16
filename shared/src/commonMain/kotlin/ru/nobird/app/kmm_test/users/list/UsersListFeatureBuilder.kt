package ru.nobird.app.kmm_test.users.list

import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.kmm_test.users.list.UsersListFeature.Action
import ru.nobird.app.kmm_test.users.list.UsersListFeature.Message
import ru.nobird.app.kmm_test.users.list.UsersListFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UsersListFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(State.Idle(), UsersListReducer())
            .wrapWithActionDispatcher(UsersListDispatcher(ActionDispatcherOptions()))
}