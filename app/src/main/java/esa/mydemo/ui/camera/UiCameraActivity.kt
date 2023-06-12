package esa.mydemo.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiCameraBinding
import esa.mylibrary.utils.MyImageUtil


class UiCameraActivity : AppBaseActivity() {

    private val TAG = "UiCameraActivity";
    private lateinit var binding: ActivityUiCameraBinding

    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_camera)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)

        //接收参数
        intent.getStringExtra("type")?.apply {
            type = this
        }


        binding.switchCamera.setOnClickListener {
            switchCamera()
        }
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        when (type) {
            "image" -> {
                initPicture()
            }

            "video" -> {
                initVideo()
            }
        }

        start()
    }

    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var camera: Camera? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageCapture: ImageCapture? = null

    private fun start() {
        /*
        通过 ProcessCameraProvider 获取 cameraProvider
        cameraProvider 就是我们持有的相机实例
        */
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            startCamera()

        }, ContextCompat.getMainExecutor(this))
    }

    /**
     *      启动相机
     * */
    private fun startCamera() {
        if (cameraProvider != null) {
            cameraProvider!!.unbindAll()
            /*
            这一步是绑定预览界面，如果不需要预览界面，这一步克注释掉
            CameraX优势体验之一：预览界面可以根据开发者需求去取舍，而Camera1和Camera2则必须要预览界面
            */
            bindPreviewUseCase()
            // 这一步是绑定相机预览数据，可以获得相机每一帧的数据
//            bindAnalysisUseCase()

            when (type) {
                "image" -> {
                    bindAnalysisUseCase()
                }

                "video" -> {
//                    initVideo()
                }
            }
        }
    }

    /**
     * 绑定预览界面。不需要预览界面可以不调用
     * */
    @SuppressLint("RestrictedApi")
    private fun bindPreviewUseCase() {
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        builder.setTargetAspectRatio(AspectRatio.RATIO_16_9).build()
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider)
        //拍照
        imageCapture = ImageCapture.Builder().setFlashMode(ImageCapture.FLASH_MODE_OFF).build()
        //摄像
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()

        videoCapture = VideoCapture.Builder()
            //.setTargetRotation(previewView.getDisplay().getRotation())
            .setVideoFrameRate(25)
            .setBitRate(3 * 1024 * 1024)
            .build()

        when (type) {
            "image" -> {

                cameraProvider!!.bindToLifecycle(
                    this,
                    cameraSelector!!,
                    previewUseCase,
                    imageCapture
                )
            }

            "video" -> {

                cameraProvider!!.bindToLifecycle(
                    this,
                    cameraSelector!!,
                    previewUseCase,
                    videoCapture
                )
            }
        }
    }

    private var lastTime = 0L
    private var lastShowTime = 0L
    private var count = 0
    private var maxFrameMs = 0L
    private var minFrameMs = Long.MAX_VALUE

    private fun bindAnalysisUseCase() {
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }
        val builder = ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
        analysisUseCase = builder.build()
        analysisUseCase?.setAnalyzer(
            ContextCompat.getMainExecutor(this)
        ) { imageProxy: ImageProxy ->
            count += 1
            val currentTime = SystemClock.elapsedRealtime()
            val d = currentTime - lastShowTime
            maxFrameMs = maxFrameMs.coerceAtLeast(d)
            minFrameMs = minFrameMs.coerceAtMost(d)
            if ((currentTime - lastTime) >= 1000) {
                binding.tvFps.text = "fps: ${count}, 两帧最大：${maxFrameMs}, 最小：${minFrameMs}"
                lastTime = currentTime
                count = 0
                maxFrameMs = 0
                minFrameMs = Long.MAX_VALUE
            }
            lastShowTime = currentTime
//            Log.d(TAG,"ImageProxy: 宽高： ${imageProxy.width} * ${imageProxy.height}")

            //必须close,相机才会下发下一帧数据,否则会一直阻塞相机下发数据
            imageProxy.close()
        }
        camera = cameraProvider!!.bindToLifecycle(
            this,
            cameraSelector!!,
            analysisUseCase
        )
    }

    private fun switchCamera() {
        if (cameraProvider == null) {
            return
        }

        val newLensFacing =
            if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }

        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()

        try {
            if (cameraProvider!!.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                startCamera()
                return
            }
        } catch (e: CameraInfoUnavailableException) {
            // Falls through
        }
    }


    private fun initPicture() {

        binding.picture.setOnClickListener {
            //开始拍照
            imageCapture?.setTargetRotation(this.windowManager.defaultDisplay.rotation)
            imageCapture?.takePicture(
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageCapturedCallback() {
                    @RequiresApi(Build.VERSION_CODES.Q)
                    override fun onCaptureSuccess(image: ImageProxy) {
                        //ImageProxy 转 Bitmap

                        try {
                            var mBitmap = MyImageUtil.imageProxyToBitmap(image)

                            //根据角度做旋转
                            val matrix = Matrix()
                            matrix.postRotate(
                                image.getImageInfo().getRotationDegrees().toFloat()
                            )
                            mBitmap = Bitmap.createBitmap(
                                mBitmap,
                                0,
                                0,
                                mBitmap.getWidth(),
                                mBitmap.getHeight(),
                                matrix,
                                true
                            )

                            MyImageUtil.saveBitmap(applicationContext, mBitmap)
                            //使用完image关闭
                            image.close()
                        } catch (ex: Exception) {
                            showMessage(ex.message)
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.d(TAG, "onError: ")
                    }
                })
            // 让画面闪一下，营造拍照的感觉
            // We can only change the foreground Drawable using API level 23+ API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Display flash animation to indicate that photo was captured
                binding.root.postDelayed({
                    binding.root.foreground = ColorDrawable(Color.WHITE)
                    binding.root.postDelayed(
                        { binding.root.foreground = null }, 50L
                    )
                }, 100L)
            }
        }
    }

    private lateinit var videoCapture: VideoCapture

    private val RECORDED_FILE_NAME = "recorded_video"
    private val RECORDED_FILE_NAME_END = "video/mp4"

    @SuppressLint("RestrictedApi")
    private fun initVideo() {
        binding.video.setOnClickListener {
            startRecording()
        }
        binding.videoStop.setOnClickListener {
            videoCapture.stopRecording()
            //            // 最后通知图库更新
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                    Uri.fromFile(File(file.path))
                )
            )

        }
    }

    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        //TODO 这里省略了RECORD_AUDIO、PERMISSION_GRANTED权限的判断
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showMessage("没有权限")
            return
        }

        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            RECORDED_FILE_NAME + "_" + System.currentTimeMillis()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, RECORDED_FILE_NAME_END)

        val outputFileOptions = VideoCapture.OutputFileOptions.Builder(
            getContentResolver(),
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        videoCapture.startRecording(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    Log.i(TAG, "视频保存成功:${outputFileResults.savedUri}")
                }

                override fun onError(
                    videoCaptureError: Int,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.i(TAG, "当出现异常 cause:$cause")
                }
            }
        )
    }

}