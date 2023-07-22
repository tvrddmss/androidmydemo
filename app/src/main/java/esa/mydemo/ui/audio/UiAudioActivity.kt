package esa.mydemo.ui.audio

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiAudioBinding
import esa.mylibrary.info.DeviceInfo

class UiAudioActivity : AppBaseActivity() {


    private lateinit var binding: ActivityUiAudioBinding
    private lateinit var viewModel: UiAudioActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_audio)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UiAudioActivityViewModel::class.java]
        viewModel.setContext(binding.root.context)

        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)

        doPlay(0)
    }


    private val TAG = "AudioActivity"

    private lateinit var mediaPlayer: MediaPlayer

    private var preparedListener: MediaPlayer.OnPreparedListener =
        MediaPlayer.OnPreparedListener { }

    private var errorListener: MediaPlayer.OnErrorListener =
        MediaPlayer.OnErrorListener { mediaPlayer: MediaPlayer, i: Int, i1: Int ->
            true
        }

    /**
     * 播放音频
     *
     * @param raw 资源文件id
     */
    private fun doPlay(raw: Int) {
        try {
//            mediaPlayer = MediaPlayer.create(applicationContext, raw);
//            if (mediaPlayer == null) {
//                Log.e(TAG, "mediaPlayer is null");
//                return;
//            }
//
//            mediaPlayer.setOnErrorListener(errorListener);
//            mediaPlayer.setOnPreparedListener(preparedListener);


            val assetManager: AssetManager = this.getAssets()
            val assetFileDescriptor: AssetFileDescriptor =
                assetManager.openFd("sound/黄色月亮-苏慧伦.320.mp3")
            if (assetFileDescriptor != null) {
                if (!this::mediaPlayer.isInitialized) {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                    mediaPlayer.setVolume(0.3f, 0.3f)
                    mediaPlayer.prepare()
                }
                mediaPlayer.start()
            }

            initVisualizer()
        } catch (e: Exception) {
            Log.e(TAG, e.message!!);
        }
    }

    /**
     * 获取MediaPlayerId
     * 可视化类Visualizer需要此参数
     * @return  MediaPlayerId
     */
    fun getMediaPlayerId(): Int {
        return mediaPlayer.getAudioSessionId()
    }


    private val dataCaptureListener: Visualizer.OnDataCaptureListener = object :
        Visualizer.OnDataCaptureListener {
        override fun onWaveFormDataCapture(
            visualizer: Visualizer,
            waveform: ByteArray,
            samplingRate: Int
        ) {
            Log.d(TAG, waveform.toString())
//            audioView.post(Runnable { audioView.setWaveData(waveform) })
        }

        override fun onFftDataCapture(visualizer: Visualizer, fft: ByteArray, samplingRate: Int) {
            Log.d(TAG, fft.toString())
//            audioView2.post(Runnable { audioView2.setWaveData(fft) })
        }
    }
    lateinit var visualizer: Visualizer
    private fun initVisualizer() {
        try {
            val mediaPlayerId: Int = getMediaPlayerId()//mediaPlayer.audioSessionId//mediaPlayer.getMediaPlayerId()

            if (this::visualizer.isInitialized) {
                visualizer.release()
            }
            visualizer = Visualizer(mediaPlayerId)
            /**
             * 可视化数据的大小： getCaptureSizeRange()[0]为最小值，getCaptureSizeRange()[1]为最大值
             */
            val captureSize = Visualizer.getCaptureSizeRange()[1]
            val captureRate = Visualizer.getMaxCaptureRate() * 3 / 4
            visualizer.setCaptureSize(captureSize)
            visualizer.setDataCaptureListener(dataCaptureListener, captureRate, true, true)
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED)
            visualizer.setEnabled(true)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "请检查录音权限")
        }
    }
}