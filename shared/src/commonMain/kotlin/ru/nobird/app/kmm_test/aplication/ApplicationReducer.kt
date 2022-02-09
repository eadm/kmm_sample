package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State

class ApplicationReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when(message) {
            Message.NavigateClick ->
                if (state is State.Screen) {
                    state to setOf(Action.ChangeFeature(ApplicationFeature.Feature.USERS_DETAIL))
                } else {
                    null
                }
            is Message.FeatureChanged ->
                if (state is State.Screen) {
                    state.copy(feature = message.feature) to emptySet()
                } else {
                    null
                }
        } ?: state to emptySet()
}