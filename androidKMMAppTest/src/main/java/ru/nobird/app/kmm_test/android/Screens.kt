package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.chrynan.parcelable.android.getParcelable
import com.chrynan.parcelable.core.Parcelable
import kotlinx.serialization.ExperimentalSerializationApi
import ru.nobird.app.kmm_test.data.model.User

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getViewModel()
        MainContent(viewModel)
    }
}


@ExperimentalSerializationApi
data class DetailsScreen(val bundle: Bundle) : Screen {

    @Composable
    override fun Content() {
        val parcelable = Parcelable.Default
        val userDetails = bundle.getParcelable<User>(USER_KEY, parcelable)!!
        DetailsContent(userDetails = userDetails)
    }

    companion object {
        const val USER_KEY = "user"
    }
}
