package ru.nobird.app.kmm_test

import ru.nobird.android.core.model.PaginationDirection
import ru.nobird.android.core.model.distinct


class Greeting {
    fun greeting(): String {
        val array = longArrayOf(1, 2, 2).distinct()
        
        return "Hello, ${PaginationDirection.NEXT} ${array.joinToString()} ${Platform().platform}!"
    }
}