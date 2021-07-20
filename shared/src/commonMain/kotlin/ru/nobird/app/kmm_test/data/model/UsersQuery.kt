package ru.nobird.app.kmm_test.data.model

data class UsersQuery(
    val page: Int = 1,
    val pageSize: Int = 10,
    val userName: String? = null
)
