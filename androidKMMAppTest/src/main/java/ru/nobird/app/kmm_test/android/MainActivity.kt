package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.app.kmm_test.android.databinding.ActivityMainBinding
import ru.nobird.app.kmm_test.user_list.UsersListFeature
import ru.nobird.app.kmm_test.user_list.UsersListFeatureBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val usersAdapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val usersListFeature = UsersListFeatureBuilder.build()

        usersListFeature.addStateListener(this::setState)

        viewBinding.button.setOnClickListener {
            usersListFeature.onNewMessage(UsersListFeature.Message.Init())
        }

        setState(usersListFeature.state)
        viewBinding.usersList.adapter = usersAdapter
    }

    private fun setState(state: UsersListFeature.State) {
        // TODO: 7/21/21 change to paged list
        usersAdapter.updateList((state as? UsersListFeature.State.Data)?.users)
    }
}
