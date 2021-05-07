package ru.nobird.app.kmm_test.feature

interface SampleFeature {
    sealed interface State {
        data class Data(val counter: Int) : State
    }

    sealed interface Message {
        object IncCounterClicked : Message
        data class CounterUpdated(val newCounter: Int) : Message
    }

    sealed interface Action {
        data class IncCounter(val currentCounter: Int) : Action
    }
}