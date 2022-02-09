package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.app.kmm_test.android.ui.KMMSample
import ru.nobird.app.kmm_test.aplication.ApplicationFeatureBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KMMSample(feature = ApplicationFeatureBuilder.build())
        }
    }
}
