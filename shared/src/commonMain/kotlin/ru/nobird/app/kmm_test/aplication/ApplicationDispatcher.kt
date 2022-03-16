package ru.nobird.app.kmm_test.aplication

import ru.nobird.app.presentation.redux.dispatcher.ActionDispatcher
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Action
import ru.nobird.app.kmm_test.aplication.ApplicationFeature.Message

class ApplicationDispatcher : ActionDispatcher<Action, Message> {
    private var messageListener: ((Message) -> Unit)? = null

    override fun handleAction(action: Action) {

    }

    override fun setListener(listener: (message: Message) -> Unit) {
        messageListener = listener
    }

    override fun cancel() {
        messageListener = null
    }
}