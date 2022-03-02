package ru.nobird.app.kmm_test.android

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

class SavableComposeState<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    defaultValue: T
) {
    private var _state by mutableStateOf(
        savedStateHandle.get<T>(key) ?: defaultValue
    )

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): T {
        Log.d("TAG", "getValue: $_state")
        return _state
    }

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T
    ) {
        _state = value
        Log.d("TAG", "setValue: $value")
        savedStateHandle.set(key, value)
    }
}