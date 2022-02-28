package ru.nobird.app.kmm_test.android

import androidx.lifecycle.ViewModel
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder

class MainViewModel: ViewModel() {
    private val _feature = UsersListFeatureBuilder.build()
    val feature = _feature
}
