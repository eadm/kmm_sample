package ru.nobird.app.kmm_test.android

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NavViewModel : ViewModel() {
    var backStack = mutableStateListOf<Screens>()
        private set

    val current = derivedStateOf {
        top()
    }

    fun push(screen: Screens) {
        backStack.add(screen)
    }

    fun pop() {
        backStack.removeLast()
    }

    private fun top(): Screens? =
       backStack.lastOrNull()


    fun backEnabled(): Boolean = backStack.size > 1

}
