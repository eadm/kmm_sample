package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chrynan.parcelable.core.Parcelable
import com.chrynan.parcelable.core.decodeFromBundle
import com.chrynan.parcelable.core.encodeToBundle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import java.util.ArrayList

@ExperimentalSerializationApi
class NavViewModel<T : Any>(
    savedStateHandle: SavedStateHandle,
    private val serializer: KSerializer<T>
) : ViewModel() {
    companion object {
        private const val NAV_KEY = "nav_key"
    }

    private val parcelable: Parcelable = Parcelable.Default

    private var backStack by SavableComposeState<ArrayList<Bundle>>(
        savedStateHandle,
        NAV_KEY,
        arrayListOf()
    )

    private val mutableBs = backStack.toMutableStateList()

    val isNullOrEmpty by derivedStateOf {
        mutableBs.isNullOrEmpty()
    }

    val current by derivedStateOf {
        top()
    }

    fun push(screen: T) {
        mutableBs.add(parcelable.encodeToBundle(value = screen, serializer = serializer))
        backStack = ArrayList(mutableBs.toList())
    }

    fun pop() {
        mutableBs.removeLast()
        backStack = ArrayList(mutableBs.toList())
    }

    private fun top(): T? =
        mutableBs.lastOrNull()
            ?.let { parcelable.decodeFromBundle(it, deserializer = serializer) }

    fun backEnabled(): Boolean = mutableBs.size > 1

}
