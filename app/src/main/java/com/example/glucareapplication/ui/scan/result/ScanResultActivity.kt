package com.example.glucareapplication.ui.scan.result

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.example.glucareapplication.R
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.databinding.ActivityAuthBinding
import com.example.glucareapplication.databinding.ActivityPreviewImageBinding
import com.example.glucareapplication.databinding.ActivityScanResultBinding
import com.example.glucareapplication.feature.auth.data.source.local.preferences.UserPreferences
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import com.example.glucareapplication.ui.scan.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Error
import kotlin.concurrent.timer

@AndroidEntryPoint
class ScanResultActivity : AppCompatActivity() {

    private var _binding: ActivityScanResultBinding? = null
    private val binding get() = _binding!!

    private val scanViewModel: ScanViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(applicationContext)
        supportActionBar?.hide()

        val data = intent.getParcelableExtra<SavePredictResponse>("data") as SavePredictResponse
        val decodedString = Base64.decode(data.image, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.apply {
            ivPreviewImage.setImageBitmap(
                decodedByte
            )
            tvResult.text = data.result
        }

        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = millisUntilFinished.toString()
            }

            override fun onFinish() {
                this@ScanResultActivity.finish()
            }
        }
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        val TAG = "ScanResultActivity"
    }
}