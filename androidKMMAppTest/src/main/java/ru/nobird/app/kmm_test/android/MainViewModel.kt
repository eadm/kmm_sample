package ru.nobird.app.kmm_test.android

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _feature = UsersListFeatureBuilder.build()
    val feature: Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>
        get() = _feature
}