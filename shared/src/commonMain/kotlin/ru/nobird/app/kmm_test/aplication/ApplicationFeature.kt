package ru.nobird.app.kmm_test.aplication

sealed interface ApplicationFeature {
    sealed class Feature {
        object UserList : Feature()
        data class UserDetails(val data: String) : Feature()
    }

    sealed interface State {
        val stack: ArrayDeque<Feature>
        data class Screen(
            val feature: Feature,
            override val stack: ArrayDeque<Feature>
        ) : State
    }

    sealed interface Message {
        data class Navigate(val feature: Feature) : Message
        data class FeatureChanged(val feature: Feature) : Message
        object BackPressed : Message
    }

    sealed interface Action {
        data class ChangeFeature(val feature: Feature) : Action
    }
}