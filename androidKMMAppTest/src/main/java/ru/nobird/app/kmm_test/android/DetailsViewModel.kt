package ru.nobird.app.kmm_test.android

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nobird.app.kmm_test.user.UserFeature
import ru.nobird.app.kmm_test.user.UserFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {
    private val _feature = UserFeatureBuilder.build()
    val feature: Feature<UserFeature.State, UserFeature.Message, UserFeature.Action>
        get() = _feature
}