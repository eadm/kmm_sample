package ru.nobird.app.kmm_test.android

import com.alphicc.brick.TreeRouter
import ru.nobird.app.kmm_test.data.model.User
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature

class MainViewModel(private val router: TreeRouter){
    private val _feature = UsersListFeatureBuilder.build()
    val feature: Feature<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action>
        get() = _feature

    fun openDetails(user: User) {
        router.addScreen(Screens.detailsScreen, user)
    }
}
