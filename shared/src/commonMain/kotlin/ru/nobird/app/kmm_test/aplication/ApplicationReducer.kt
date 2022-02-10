package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.presentation.redux.reducer.StateReducer
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.State

class ApplicationReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when(message) {
            is Message.Navigate ->
                if (state is State.Screen) {
                    state to setOf(Action.ChangeFeature(message.feature))
                } else {
                    null
                }
            is Message.FeatureChanged ->
                if (state is State.Screen) {
                    state.copy(
                        feature = message.feature,
                        stack = ArrayDeque<ApplicationFeature.Feature>()
                            .apply {
                                addAll(state.stack)
                                addFirst(message.feature)
                            }
                    ) to emptySet()
                } else {
                    null
                }
            Message.BackPressed -> {
                if (state is State.Screen) {
                    state.copy(
                        stack = ArrayDeque<ApplicationFeature.Feature>()
                            .apply {
                                addAll(state.stack)
                                removeFirst()
                            }
                    ) to emptySet()
                } else {
                    null
                }
            }
        } ?: state to emptySet()
}