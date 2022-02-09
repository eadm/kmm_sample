package ru.nobird.app.kmm_test.users.detail

import ru.nobird.app.kmm_test.base.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Action
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.Message
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object UserDetailsFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(State.Idle, UserDetailsReducer())
            .wrapWithActionDispatcher(UserDetailsDispatcher(ActionDispatcherOptions()))
}