package ru.nobird.app.kmm_test.android

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Component(modules = [AppModule::class])
@JvmSuppressWildcards
interface AppComponent {


    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

    fun mainComponentBuilder(): MainComponent.Builder

}

@Module
class AppModule