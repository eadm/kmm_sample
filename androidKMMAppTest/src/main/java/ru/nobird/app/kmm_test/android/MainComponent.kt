package ru.nobird.app.kmm_test.android

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton
import kotlin.reflect.KClass

@ExperimentalSerializationApi
@Subcomponent(modules = [ScreensModule::class])
interface MainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }

    fun inject(mainActivity: MainActivity)

}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ScreenKey(val value: KClass<out Screen>)

@Module
abstract class ScreensModule {

    @Binds
    @IntoMap
    @ScreenKey(Screen.Main::class)
    abstract fun provideMainScreen(screenFactory: MainScreenFactory): ScreenFactory<Screen.Main>

    @Binds
    @IntoMap
    @ScreenKey(Screen.Details::class)
    abstract fun provideDetailsScreen(screenFactory: DetailsScreenFactory): ScreenFactory<Screen.Details>

    @Binds
    @IntoMap
    @ScreenKey(Screen.Web::class)
    abstract fun provideWebScreen(screenFactory: WebScreenFactory): ScreenFactory<Screen.Web>

    @Multibinds
    abstract fun screensMap(): Map<Class<out Screen>, @JvmSuppressWildcards ScreenFactory<Screen>>
}
