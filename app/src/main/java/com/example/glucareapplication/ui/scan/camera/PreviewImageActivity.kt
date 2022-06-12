package com.example.glucareapplication.ui.scan.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.example.glucareapplication.R
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.databinding.ActivityPreviewImageBinding
import com.example.glucareapplication.feature.auth.data.source.local.preferences.UserPreferences
import com.example.glucareapplication.ui.scan.ScanViewModel
import com.example.glucareapplication.ui.scan.result.ScanResultActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class PreviewImageActivity : AppCompatActivity() {

    private var _binding: ActivityPreviewImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreferences: UserPreferences
    private val scanViewModel: ScanViewModel by viewModels()
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferences = UserPreferences(applicationContext)

        supportActionBar?.hide()

        val myFile = intent.getSerializableExtra("picture") as File
        val result = BitmapFactory.decodeFile(myFile.path)
        getFile = myFile
        binding.ivPreviewImage.setImageBitmap(result)
        postPredict()

    }

    private fun postPredict() {
        getFile?.let {

            val requestImageFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                it.name,
                requestImageFile
            )

            userPreferences.getUser().asLiveData().observe(this) { user ->
                scanViewModel.postPredict("Bearer ${user[1]}", user[0], imageMultipart).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.pbImage.visibility = View.VISIBLE
                            binding.btnCamera.visibility = View.INVISIBLE
                            binding.btnCheck.visibility = View.INVISIBLE
                        }
                        is Result.Success -> {
                            val intent = Intent(this,ScanResultActivity::class.java)
                            intent.putExtra("data",result.data)
                            startActivity(intent)
                            this.finish()
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "failed when processing image, Try again!", Toast.LENGTH_LONG).show()
                            this.finish()
                        }
                    }
                }
            }
        } ?: Toast.makeText(
            this,
            "Please Take Picture first!!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}