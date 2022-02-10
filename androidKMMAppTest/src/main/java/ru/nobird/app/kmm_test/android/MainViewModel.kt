package ru.nobird.app.kmm_test.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature

class MainViewModel : ViewModel() {
    private val _feature = MutableLiveData(UsersListFeatureBuilder.build())
    val feature: LiveData<Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>>
        get() = _feature
}