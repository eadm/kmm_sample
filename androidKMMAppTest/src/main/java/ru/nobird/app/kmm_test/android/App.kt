package ru.nobird.app.kmm_test.android

import android.app.Application
import ru.nobird.app.kmm_test.aplication.ApplicationFeatureBuilder

class App : Application() {
    val feature by lazy { ApplicationFeatureBuilder.build() }
}