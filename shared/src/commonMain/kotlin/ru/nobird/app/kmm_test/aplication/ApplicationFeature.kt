package ru.nobird.app.kmm_test.aplication

sealed interface ApplicationFeature {
    enum class Feature {
        USERS_LIST,
        USERS_DETAIL
    }

    sealed interface State {
        val stack: ArrayDeque<Feature>
        data class Screen(
            val feature: Feature,
            override val stack: ArrayDeque<Feature>
        ) : State
    }

    sealed interface Message {
        data class NavigateClick(val feature: Feature) : Message
        data class FeatureChanged(val feature: Feature) : Message
        object BackPressed : Message
    }

    sealed interface Action {
        data class ChangeFeature(val feature: Feature) : Action
    }
}