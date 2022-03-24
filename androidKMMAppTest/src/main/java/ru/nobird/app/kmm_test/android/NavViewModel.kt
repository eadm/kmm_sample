package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.*
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

    private val mutableBs = mutableStateOf<List<Bundle>>(backStack)

    val isNullOrEmpty by derivedStateOf {
        mutableBs.value.isNullOrEmpty()
    }

    val current by derivedStateOf {
        top()
    }

    fun push(screen: Screens) {
        mutableBs.value += parcelable.encodeToBundle(screen)
        backStack = ArrayList(mutableBs.value)
    }

    fun pop() {
        mutableBs.value = mutableBs.value.dropLast(mutableBs.value.size)
        backStack = ArrayList(mutableBs.value)
    }

    private fun top(): Screens? =
        mutableBs.value.lastOrNull()?.let { parcelable.decodeFromBundle(it) }

    fun backEnabled(): Boolean = mutableBs.value.size > 1

}
