package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import ru.nobird.app.kmm_test.android.ui.addCancellable
import ru.nobird.app.kmm_test.android.ui.users.list.MainScreen
import ru.nobird.app.kmm_test.users.list.UsersListFeatureBuilder
import ru.nobird.app.kmm_test.android.ui.users.detail.UserDetailsComposable
import ru.nobird.app.kmm_test.aplication.ApplicationFeature
import ru.nobird.app.kmm_test.aplication.ApplicationFeatureBuilder
import ru.nobird.app.kmm_test.users.detail.UserDetailsFeatureBuilder
import ru.nobird.app.presentation.redux.feature.Feature

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KMMSample(feature = ApplicationFeatureBuilder.build())
        }
    }

    @Composable
    private fun KMMSample(feature: Feature<ApplicationFeature.State, ApplicationFeature.Message, ApplicationFeature.Action>) {
        var featureState by remember { mutableStateOf(feature.state) }
        
        LocalLifecycleOwner.current.lifecycle
            .addCancellable {
                feature.addStateListener { featureState = it }
                feature
            }

        when (val state = featureState) {
            is ApplicationFeature.State.Screen -> 
                when(state.feature) {
                    ApplicationFeature.Feature.USERS_LIST -> {
                        MainScreen(usersListFeature = UsersListFeatureBuilder.build()) {
                            feature.onNewMessage(ApplicationFeature.Message.NavigateClick)
                        }
                    }
                    ApplicationFeature.Feature.USERS_DETAIL ->
                        UserDetailsComposable(userFeature = UserDetailsFeatureBuilder.build()) {
                            //todo
                        }
                }
        }
    }
}