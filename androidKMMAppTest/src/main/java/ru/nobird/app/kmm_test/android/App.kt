package ru.nobird.app.kmm_test.android

import android.app.Application
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class App : Application() {

    companion object {
        lateinit var application: App
            private set

        fun component(): AppComponent =
            application.component
    }

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        application = this
        component = DaggerAppComponent.builder().context(application).build()
    }
}