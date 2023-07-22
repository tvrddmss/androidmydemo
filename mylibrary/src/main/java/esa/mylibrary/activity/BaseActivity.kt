package esa.mylibrary.activity

import android.Manifest
import android.app.ActivityManager
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import esa.mylibrary.R
import esa.mylibrary.config.Config
import esa.mylibrary.utils.MySharedPreferences
import esa.mylibrary.utils.log.MyLog

open class BaseActivity : AppCompatActivity() {
    val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var isFront = false //是否是显示页面,当前页面是否在最上面，是否已完成初始化

    //是否启用双击退出，还是保持原来的返回键
    var isDoubleClickExit = false

    //双击返回键功能-0:后台运行，1：退出，默认后台运行
    var backOrExit = 0
    override fun onCreate(arg0: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(arg0)
        _init()
        //初始化屏幕大小兼容,等比缩放
//        DensityUtil.setDensity(getApplication(),this);

        //判断是否第一次打开APP--避免每个页面都请求权限，导致onResume执行两次
        if (MySharedPreferences.getIsFirstStart()) {
            //第一次打开APP获取权限
            requestPermissions()
        } else {
            Config.initByPermissionEnd(this)
        }
    }

    //初始化方法-为避免函数名冲突-
    private fun _init() {

        //初始化网络链接的监控
//        init_dialog_nonetwork();
//        MyLog.d("初始化网络监控");

        //初始化Loading的显示
        MyLog.d("初始化Loading")

        //保持屏幕长亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        MyLog.d("保持屏幕长亮")

//        setStausBarTransparent();
    }

    override fun onResume() {
        super.onResume()
        isFront = true
    }

    override fun onPause() {
        super.onPause()
        isFront = false
    }

    override fun onDestroy() {
        super.onDestroy()
        MyLog.d(this.javaClass.toString() + "销毁")
    }

    //region 没有网络信息的提示
    private var dialog_nonetwork // 显示没有网络的Dialog
            : Dialog? = null
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //更新UI
            if (isFront) {
                when (msg.what) {
                    0 -> shownonetwork()
                    1 -> closenonetwork()
                }
            }
        }
    }

    /* 初始化没有网络对话框 */
    private fun init_dialog_nonetwork() {
        dialog_nonetwork = Dialog(this, R.style.base_loading)
        dialog_nonetwork!!.setContentView(R.layout.base_nonetwork)
        dialog_nonetwork!!.setCancelable(false)
        val window = dialog_nonetwork!!.window
        val params = window!!.attributes
        //        params.y = -PhoneInfo.intScreenHeight / 3;
        /* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */dialog_nonetwork!!.setCanceledOnTouchOutside(
            false
        ) // 设置点击Dialog外部任意区域关闭Dialog
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.requestNetwork(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    ///网络不可用的情况下的方法
                    //更新UI方法  1
                    val message = Message()
                    message.what = 0
                    mHandler.sendMessage(message)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    ///网络可用的情况下的方法
                    //更新UI方法  1
                    val message = Message()
                    message.what = 1
                    mHandler.sendMessage(message)
                }
            })
    }

    /* 显示没有网络对话框 */
    protected fun shownonetwork() {
        if (dialog_nonetwork != null && !dialog_nonetwork!!.isShowing) dialog_nonetwork!!.show()
    }

    /* 关闭没有网络对话框 */
    protected fun closenonetwork() {
        if (dialog_nonetwork != null && dialog_nonetwork!!.isShowing) dialog_nonetwork!!.dismiss()
    }

    //endregion

    //region 双击退出APP
    private var exitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        // System.out.println(keyCode);
        return if (keyCode == KeyEvent.KEYCODE_BACK
            && event.action == KeyEvent.ACTION_DOWN
        ) {
            val mActivityManager =
                this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val topActivityName = mActivityManager.getRunningTasks(1)[0].topActivity!!.className
            if ( //                    topActivityName.equals("esa.zjgcdy.main.Main")
                isDoubleClickExit) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(
                        applicationContext, "再按一次退出程序",
                        Toast.LENGTH_SHORT
                    ).show()
                    exitTime = System.currentTimeMillis()
                } else {
                    if (backOrExit == 0) {
                        //后台运行
                        moveTaskToBack(false)
                    } else {
                        //直接退出
                        finish()
                        System.exit(0)
                    }
                }
                true
            } else {
                //有网络监控提示时不能关闭
                if (dialog_nonetwork != null && dialog_nonetwork!!.isShowing) {
                    true
                } else {
                    super.onKeyDown(keyCode, event)
                }
            }
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
    //endregion

    /**
     * @return void
     * @description 全透明---状态局域栏不占位
     * @author Administrator
     * @time 2023/04/07 14:52
     */
    fun setStausBarTransparent() {
        /**
         * 全透状态栏
         */
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明--没效果
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    /**
     * @return void
     * @description 半透明--状态栏不占位---样式中可设置状态栏的半透明色
     * @author Administrator
     * @time 2023/04/07 14:16
     */
    fun setStausBarTranslucent() {

//        MyLog.d("状态栏半透明" + Build.VERSION.SDK_INT);
        /**
         * 半透明状态栏
         */
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            MyLog.d("状态栏半透明");
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明--没效果
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            //            MyLog.d("状态栏半透明");
        }
    }

    //endregion

    //region permission
    protected fun requestPermissions() {
        /**
         * 以 Android 10（API 级别 29）为目标平台，请停用分区存储，继续使用适用于 Android 9 及更低版本的方法来执行此操作。
         * 在清单文件 application节点内添加 requestLegacyExternalStorage属性
         * android:requestLegacyExternalStorage="true"
         *
         * 当前项目超过SDK>=30所以，以下只能请求到媒体文件的访问权限，如果需要所有文件访问需要手动调整app权限为可以访问所有文件
         * Manifest.permission.MANAGE_EXTERNAL_STORAGE,
         * Manifest.permission.WRITE_EXTERNAL_STORAGE,
         * Manifest.permission.READ_EXTERNAL_STORAGE,
         *
         */
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,  //                Manifest.permission.WRITE_SETTINGS,//-1
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,  //                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,//-1
                //                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,//-1
                Manifest.permission.CAMERA,  //                Manifest.permission.REQUEST_INSTALL_PACKAGES,//-1
                //                Manifest.permission.SYSTEM_ALERT_WINDOW,//-1
                //                Manifest.permission.INSTALL_PACKAGES,//-1
                Manifest.permission.RECORD_AUDIO
            ), REQUEST_CODE_ASK_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                for (code in grantResults) {
                    if (code != PackageManager.PERMISSION_GRANTED) {
                        MyLog.e("没有获取权限！")
                        break
                    }
                }
                Config.initByPermissionEnd(this)
            }

            else -> {}
        }
    }
    //endregion

}