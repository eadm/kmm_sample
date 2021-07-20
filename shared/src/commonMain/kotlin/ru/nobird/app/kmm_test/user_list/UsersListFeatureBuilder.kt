package ru.nobird.app.kmm_test.user_list

import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Action
import ru.nobird.app.kmm_test.user_list.UsersListFeature.Message
import ru.nobird.app.kmm_test.user_list.UsersListFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UsersListFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(State.Idle, UserListReducer())
            .wrapWithActionDispatcher(UsersListDispatcher(ActionDispatcherOptions()))
}