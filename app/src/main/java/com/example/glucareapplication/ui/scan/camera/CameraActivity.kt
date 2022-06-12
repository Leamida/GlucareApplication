package com.example.glucareapplication.ui.scan.camera;

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.example.glucareapplication.R
import com.example.glucareapplication.core.line_chart.utils.UriTo
import com.example.glucareapplication.databinding.ActivityCameraBinding
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnCaptureImage.setOnClickListener { takePhoto() }
        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun cropImage(bitmap: Bitmap, cameraFrame: View, cropRectFrame: View,): ByteArray {
        val scaleFactor: Double; val widthOffset: Double; val heightOffset: Double

        if (cameraFrame.height * bitmap.width > cameraFrame.height * bitmap.width) {
            scaleFactor = (bitmap.width).toDouble() / (cameraFrame.width).toDouble()
            widthOffset = 0.0
            heightOffset = (bitmap.height - cameraFrame.height * scaleFactor) / 2
        } else {
            scaleFactor = (bitmap.height).toDouble() / (cameraFrame.height).toDouble()
            widthOffset = (bitmap.width - cameraFrame.width * scaleFactor) / 2
            heightOffset = 0.0
        }

        val newX = cropRectFrame.left * scaleFactor + widthOffset
        val newY = cropRectFrame.top * scaleFactor + heightOffset
        val width = cropRectFrame.width * scaleFactor
        val height = cropRectFrame.height * scaleFactor
        val bmc = Bitmap.createBitmap(bitmap, (newX).toInt(), (newY).toInt(), (width).toInt(), (height).toInt())
        val rotateBmc = UriTo.rotateBitmap(bmc,cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)

        val stream = ByteArrayOutputStream()
        rotateBmc.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        ) //100 is the best quality possible
        return stream.toByteArray()

    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = UriTo.createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        resources.getString(R.string.failed_getting_image),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity,PreviewImageActivity::class.java)

                    val croppedImage = cropImage(
                        BitmapFactory.decodeFile(photoFile.path),
                        binding.viewFinder,
                        binding.vPlaceholder
                    )
                    val overWrite = FileOutputStream(photoFile, false)

                    overWrite.write(croppedImage)
                    overWrite.flush()
                    overWrite.close()
                    BitmapFactory.decodeByteArray(croppedImage, 0, croppedImage.size)
                    intent.putExtra("picture", photoFile)
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    resources.getString(R.string.failed_launch_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}