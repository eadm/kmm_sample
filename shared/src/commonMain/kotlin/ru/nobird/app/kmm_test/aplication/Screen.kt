package ru.nobird.app.kmm_test.aplication

// TODO: generic возможно создаст больше проблем, чем принисет пользы
data class Screen<T>(
    val screenState: ApplicationFeature.State.ScreenState,
    val arg: T?
)
