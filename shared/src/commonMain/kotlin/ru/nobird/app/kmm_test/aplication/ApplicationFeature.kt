package ru.nobird.app.kmm_test.aplication

sealed interface ApplicationFeature {
    enum class Feature {
        USERS_LIST,
        USERS_DETAIL
    }

    sealed interface State {
        data class Screen(val feature: Feature) : State
    }

    sealed interface Message {
        object NavigateClick : Message
        data class FeatureChanged(val feature: Feature) : Message
    }

    sealed interface Action {
        data class ChangeFeature(val feature: Feature) : Action
    }
}