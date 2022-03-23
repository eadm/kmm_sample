package ru.nobird.app.kmm_test.android

import kotlinx.serialization.Serializable
import ru.nobird.app.kmm_test.data.model.User

@Serializable
sealed class Screens {
    @Serializable
    object Main: Screens()
    @Serializable
    data class Details(val user: User): Screens()
    @Serializable
    data class Web(val url: String): Screens()
}
