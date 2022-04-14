package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder

class MainViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val QUERY_KEY = "query_key"
    }

    private val _feature = UsersListFeatureBuilder.build()
    val feature = _feature

    var query by mutableStateOf(SavableComposeState(savedStateHandle, QUERY_KEY, ""))

}
