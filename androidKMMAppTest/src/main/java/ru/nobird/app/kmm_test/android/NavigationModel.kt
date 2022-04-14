package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.chrynan.parcelable.core.Parcelable
import com.chrynan.parcelable.core.decodeFromBundle
import com.chrynan.parcelable.core.encodeToBundle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import java.util.ArrayList
import javax.inject.Inject

@ExperimentalSerializationApi
class NavigationModel<T : Any>(
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

    val isEmpty by derivedStateOf {
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

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(handle: SavedStateHandle): T
}

@ExperimentalSerializationApi
class NavAssistedVMFactory @Inject constructor() :
    ViewModelAssistedFactory<NavigationModel<Screen>> {
    override fun create(handle: SavedStateHandle): NavigationModel<Screen> {
        return NavigationModel(handle, Screen.serializer())
    }
}

class Factory<out V : ViewModel>(
    private val viewModelFactory: ViewModelAssistedFactory<V>,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return viewModelFactory.create(handle) as T
    }
}

