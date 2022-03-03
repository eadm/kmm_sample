package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chrynan.parcelable.core.Parcelable
import com.chrynan.parcelable.core.decodeFromBundle
import com.chrynan.parcelable.core.encodeToBundle
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.ArrayList

@ExperimentalSerializationApi
class NavViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val NAV_KEY = "nav_key"
    }
    private val parcelable: Parcelable = Parcelable.Default


    private var backStack by SavableComposeState<ArrayList<Bundle>>(
        savedStateHandle,
        NAV_KEY,
        arrayListOf()
    )
        private set

    val bs = backStack.toMutableStateList()

    val current = derivedStateOf {
        top()
    }

    fun push(screen: Screens) {
        bs.add(parcelable.encodeToBundle(screen))
        backStack = ArrayList(bs.toList())
    }

    fun pop() {
        bs.removeLast()
        backStack = ArrayList(bs.toList())
    }

    private fun top(): Screens? =
        bs.lastOrNull()?.let { parcelable.decodeFromBundle(it) }

    fun backEnabled(): Boolean = bs.size > 1

}
