package esa.mydemo.ui.tv

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.datasource.rtmp.RtmpDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiTvBinding
import esa.mylibrary.info.DeviceInfo


class UiTvActivity : AppBaseActivity() {

    private lateinit var binding: ActivityUiTvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_tv)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiTvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)

        initUi()

    }

    private fun initUi() {
//        binding.textinput.setText("http://vfx.mtime.cn/Video/2021/07/10/mp4/210710171112971120.mp4");

        binding.textinput.setText("rtmp://192.168.50.236/live/livestream")
        binding.SwitchCamerabutton.setOnClickListener(View.OnClickListener {

        })


        initTv()

    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun initTv() {

        try {


            var uri = Uri.parse(binding.textinput.text.toString())

            var player = ExoPlayer.Builder(this).build()
            binding.playerView.player = player

//            if ("rtmp".equals(url.substring(0, 4))) {
            var dataSourceFactory = RtmpDataSourceFactory();
            var videoSource =
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

            player.addMediaSource(videoSource)
//            player.addMediaItem(MediaItem.fromUri(uri))
//            player.addMediaItem(MediaItem.fromUri("http://121.51.249.103/tlivecloud-ipv6.ysp.cctv.cn/ysp/2000203603.m3u8"))
//            } else {
//            player.addMediaItem(MediaItem.fromUri(uri))
//            }
//            player.prepare(videoSource)
            //准备完成就开始播放
            player.playWhenReady = true

        } catch (ex: Exception) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player?.clearMediaItems()
    }
}