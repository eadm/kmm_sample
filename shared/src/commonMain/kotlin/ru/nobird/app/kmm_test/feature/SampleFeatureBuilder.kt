package ru.nobird.app.kmm_test.feature

import ru.nobird.app.kmm_test.feature.SampleFeature.Action
import ru.nobird.app.kmm_test.feature.SampleFeature.Message
import ru.nobird.app.kmm_test.feature.SampleFeature.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object SampleFeatureBuilder {
    fun build(): Feature<State, Message, Action> =
        ReduxFeature(State.Data(0), SampleReducer())
            .wrapWithActionDispatcher(SampleDispatcher())
}