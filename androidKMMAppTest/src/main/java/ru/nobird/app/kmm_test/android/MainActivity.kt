package ru.nobird.app.kmm_test.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.app.kmm_test.android.databinding.ActivityMainBinding
import ru.nobird.app.kmm_test.feature.SampleFeature
import ru.nobird.app.kmm_test.feature.SampleFeatureBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sampleFeature = SampleFeatureBuilder.build()

        sampleFeature.addStateListener(this::setState)

        viewBinding.button.setOnClickListener {
            sampleFeature.onNewMessage(SampleFeature.Message.IncCounterClicked)
        }

        setState(sampleFeature.state)
    }

    private fun setState(state: SampleFeature.State) {
        viewBinding.textView.text = (state as? SampleFeature.State.Data)?.counter.toString()
    }
}
