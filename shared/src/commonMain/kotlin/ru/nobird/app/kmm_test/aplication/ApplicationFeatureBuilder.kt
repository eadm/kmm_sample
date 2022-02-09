package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher

object ApplicationFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(
            State.Screen(
                feature = ApplicationFeature.Feature.USERS_LIST,
                stack = ArrayDeque(listOf(ApplicationFeature.Feature.USERS_LIST))
            ),
            ApplicationReducer()
        ).wrapWithActionDispatcher(ApplicationDispatcher())
}