package ru.nobird.app.kmm_test.feature

import ru.nobird.android.presentation.redux.dispatcher.ActionDispatcher
import ru.nobird.app.kmm_test.feature.SampleFeature.Message
import ru.nobird.app.kmm_test.feature.SampleFeature.Action

class SampleDispatcher : ActionDispatcher<Action, Message> {
    private var messageListener: ((Message) -> Unit)? = null

    override fun handleAction(action: Action) {
        when (action) {
            is Action.IncCounter ->
                messageListener?.invoke(Message.CounterUpdated(action.currentCounter + 1))
        }
    }

    override fun setListener(listener: (message: Message) -> Unit) {
        messageListener = listener
    }

    override fun cancel() {
        messageListener = null
    }
}