package ru.nobird.app.kmm_test.android

import ru.nobird.app.kmm_test.data.model.User

sealed class Screens {
    object Main: Screens()
    data class Details(val user: User): Screens()
}
