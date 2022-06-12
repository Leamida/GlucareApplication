package com.example.glucareapplication.ui.scan.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.glucareapplication.R
import com.example.glucareapplication.core.line_chart.utils.UriTo.Companion.rotateBitmap
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.databinding.ActivityPreviewImageBinding
import com.example.glucareapplication.feature.auth.data.source.local.preferences.UserPreferences
import com.example.glucareapplication.ui.dashboard.DashboardViewModel
import com.example.glucareapplication.ui.scan.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class PreviewImageActivity : AppCompatActivity() {

    private var _binding: ActivityPreviewImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreferences: UserPreferences
    private val scanViewModel: ScanViewModel by viewModels()

    private var getFile: File? = null
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.cant_get_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferences = UserPreferences(applicationContext)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.apply {
            btnCamera.setOnClickListener { startCamera() }
            btnCheck.setOnClickListener { postPredict() }
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val croppedImage = it.data?.getByteArrayExtra("croppedImage") as ByteArray
            val bmp = BitmapFactory.decodeByteArray(croppedImage, 0, croppedImage.size)
            val result = rotateBitmap(
                bmp,
                isBackCamera
            )
            getFile = myFile

            binding.ivPreviewImage.setImageBitmap(result)
        }
    }

    private fun postPredict() {

        getFile?.let {
            reduceFileImage(it)
            val requestImageFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                it.name,
                requestImageFile
            )

            userPreferences.getUser().asLiveData().observe(this) { user ->
                scanViewModel.postPredict(user, imageMultipart).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {

                            Toast.makeText(this, result.data.predictEye.toString(), Toast.LENGTH_LONG).show()
                        }
                        is Result.Error -> {
                            Log.d(TAG,"error ${result.error}")
                            Toast.makeText(this, "error ${result.error}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }?: Toast.makeText(
            this,
            "Please Take Picture first!!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAMERA_RESULT = 200
        private const val TAG = "PreviewImageActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}