package ru.nobird.app.kmm_test.android

import kotlinx.serialization.Serializable
import ru.nobird.app.kmm_test.data.model.User

@Serializable
sealed class Screen {
    @Serializable
    object Main: Screen()
    @Serializable
    data class Details(val user: User): Screen()
    @Serializable
    data class Web(val url: String): Screen()
}
