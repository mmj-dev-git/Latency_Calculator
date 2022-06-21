package one.younite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.mobileer.domain.LatencyMain.LatencyViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LatencyClass : AppCompatActivity() {

    private val viewModelOffer: LatencyViewModel by viewModels()
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latency_class)
        button = findViewById(R.id.btn)

        button.setOnClickListener {
            viewModelOffer.getDeviceLatency(this)
        }
    }
}