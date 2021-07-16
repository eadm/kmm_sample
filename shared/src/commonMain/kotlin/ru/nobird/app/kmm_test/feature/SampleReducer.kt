package ru.nobird.app.kmm_test.feature

import ru.nobird.app.kmm_test.feature.SampleFeature.State
import ru.nobird.app.kmm_test.feature.SampleFeature.Message
import ru.nobird.app.kmm_test.feature.SampleFeature.Action
import ru.nobird.app.presentation.redux.reducer.StateReducer

class SampleReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.IncCounterClicked ->
                if (state is State.Data) {
                    state to setOf(Action.IncCounter(state.counter))
                } else {
                    null
                }

            is Message.CounterUpdated ->
                if (state is State.Data) {
                    state.copy(counter = message.newCounter) to emptySet()
                } else {
                    null
                }
        } ?: state to emptySet()
}