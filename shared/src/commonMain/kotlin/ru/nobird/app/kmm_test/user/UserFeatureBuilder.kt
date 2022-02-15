package ru.nobird.app.kmm_test.user

import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.kmm_test.user.UserFeature.Action
import ru.nobird.app.kmm_test.user.UserFeature.Message
import ru.nobird.app.kmm_test.user.UserFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UserFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(State.Idle, UserReducer())
            .wrapWithActionDispatcher(UserDispatcher(ActionDispatcherOptions()))
}