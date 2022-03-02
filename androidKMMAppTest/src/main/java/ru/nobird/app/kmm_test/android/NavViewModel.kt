package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chrynan.parcelable.core.Parcelable
import com.chrynan.parcelable.core.decodeFromBundle
import com.chrynan.parcelable.core.encodeToBundle
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class NavViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val NAV_KEY = "nav_key"
    }
    private val parcelable: Parcelable = Parcelable.Default

    var backStack by SavableComposeState<SnapshotStateList<Bundle>>(
        savedStateHandle,
        NAV_KEY,
        mutableStateListOf()
    )
        private set

    val current = derivedStateOf {
        top()
    }

    fun push(screen: Screens) {
        backStack.add(parcelable.encodeToBundle(screen))
    }

    fun pop() {
        backStack.removeLast()
    }

    private fun top(): Screens? =
        backStack.lastOrNull()?.let { parcelable.decodeFromBundle(it) }

    fun backEnabled(): Boolean = backStack.size > 1

}
