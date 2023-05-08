package esa.mylibrary.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import esa.mylibrary.R;
import esa.mylibrary.utils.MySharedPreferences;
import esa.mylibrary.utils.log.MyLog;

public class BaseActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private Boolean isFront = false;//是否是显示页面,当前页面是否在最上面，是否已完成初始化

    //是否启用双击退出，还是保持原来的返回键
    public Boolean isDoubleClickExit = false;
    //双击返回键功能-0:后台运行，1：退出，默认后台运行
    public int backOrExit = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

        _init();
        //初始化屏幕大小兼容,等比缩放
//        DensityUtil.setDensity(getApplication(),this);

        //判断是否第一次打开APP--避免每个页面都请求权限，导致onResume执行两次
        if (MySharedPreferences.getIsFirstStart()) {
            //第一次打开APP获取权限
            requestPermissions();
        }

    }

    //初始化方法-为避免函数名冲突-
    private void _init() {

        //初始化网络链接的监控
//        init_dialog_nonetwork();
//        MyLog.d("初始化网络监控");

        //初始化Loading的显示
        initLoadingDlg();
        MyLog.d("初始化Loading");

        //保持屏幕长亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MyLog.d("保持屏幕长亮");

//        setStausBarTransparent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.d(this.getClass() + "销毁");
    }


    //region 没有网络信息的提示
    private Dialog dialog_nonetwork; // 显示没有网络的Dialog

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //更新UI
            if (isFront) {
                switch (msg.what) {
                    case 0://网络不可用
                        shownonetwork();
                        break;
                    case 1://网络可用
                        closenonetwork();
                        break;
                }
            }
        }

        ;
    };

    /* 初始化没有网络对话框 */
    private void init_dialog_nonetwork() {

        dialog_nonetwork = new Dialog(this, R.style.base_loading);
        dialog_nonetwork.setContentView(R.layout.base_nonetwork);
        dialog_nonetwork.setCancelable(false);
        Window window = dialog_nonetwork.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.y = -PhoneInfo.intScreenHeight / 3;
        /* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */
        dialog_nonetwork.setCanceledOnTouchOutside(false); // 设置点击Dialog外部任意区域关闭Dialog
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                ///网络不可用的情况下的方法
                //更新UI方法  1
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                ///网络可用的情况下的方法
                //更新UI方法  1
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        });
    }

    /* 显示没有网络对话框 */
    protected void shownonetwork() {
        if (dialog_nonetwork != null && !dialog_nonetwork.isShowing())
            dialog_nonetwork.show();
    }

    /* 关闭没有网络对话框 */
    protected void closenonetwork() {
        if (dialog_nonetwork != null && dialog_nonetwork.isShowing())
            dialog_nonetwork.dismiss();
    }

    //endregion

    //region 双击退出APP
    private long exitTime = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // System.out.println(keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            String topActivityName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (
//                    topActivityName.equals("esa.zjgcdy.main.Main")
                    isDoubleClickExit
            ) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序",
                            Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    if (backOrExit == 0) {
                        //后台运行
                        moveTaskToBack(false);
                    } else {
                        //直接退出
                        finish();
                        System.exit(0);
                    }
                }
                return true;
            } else {
                //有网络监控提示时不能关闭
                if (dialog_nonetwork != null && dialog_nonetwork.isShowing()) {
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //endregion

    //region  信息栏背景

//    public void setStatusBar()
//    {
//
////        View decorView = getWindow().getDecorView();
//////        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
////        int option=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
////        decorView.setSystemUiVisibility(option);
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        MyLog.d("状态栏恢复");
//    }

    /**
     * @return void
     * @description 全透明---状态局域栏不占位
     * @author Administrator
     * @time 2023/04/07 14:52
     */
    public void setStausBarTransparent() {
        /**
         * 全透状态栏
         */
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明--没效果
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * @return void
     * @description 半透明--状态栏不占位---样式中可设置状态栏的半透明色
     * @author Administrator
     * @time 2023/04/07 14:16
     */
    public void setStausBarTranslucent() {

//        MyLog.d("状态栏半透明" + Build.VERSION.SDK_INT);
        /**
         * 半透明状态栏
         */
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            MyLog.d("状态栏半透明");

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明--没效果
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            MyLog.d("状态栏半透明");
        }
    }
    //endregion

    //region loading
    public Dialog mLoginingDlg; // 显示正在登录的Dialog
    public TextView loading_text;

    /* 初始化loading */
    private void initLoadingDlg() {

        mLoginingDlg = new Dialog(this, R.style.base_loading);
        mLoginingDlg.setContentView(R.layout.base_loading);
        loading_text = mLoginingDlg.findViewById(R.id.loading_text);
        mLoginingDlg.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                // return false;
                // if (keyCode == KeyEvent.KEYCODE_BACK
                // && event.getRepeatCount() == 0) {
                // return true;
                // } else {
                // return false;
                // }
                return onKeyDown(keyCode, event);
            }
        });// .setCancelable(false);

        Window window = mLoginingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

//        params.y = -(PhoneInfo.intScreenHeight / 6) * 2; // -199
        /* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */

        mLoginingDlg.setCanceledOnTouchOutside(false); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 显示loading */
    public void showloading() {
        if (mLoginingDlg != null)
            loading_text.setText(getResources().getText(R.string.base_loading));
        mLoginingDlg.show();
    }

    /* 显示loading */
    public void showloading(String text) {
        if (mLoginingDlg != null)
            loading_text.setText(text);
        mLoginingDlg.show();
    }

    /* 关闭loading */
    public void closeloading() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }

    //endregion

    //region permission

    protected void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.WRITE_SETTINGS,//-1
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,//-1
//                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,//-1
                Manifest.permission.CAMERA,
//                Manifest.permission.REQUEST_INSTALL_PACKAGES,//-1
//                Manifest.permission.SYSTEM_ALERT_WINDOW,//-1
//                Manifest.permission.INSTALL_PACKAGES,//-1
        }, REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int code : grantResults) {
                    if (code != PackageManager.PERMISSION_GRANTED) {
                        MyLog.e("没有获取权限！");
                        break;
                    }
                }
//                _init();
            default:
                break;
        }
    }

    //endregion

    //region message

    /**
     * @param message
     * @return null
     * @description
     * @author Administrator
     * @time 2023/03/29 13:44
     */
    protected void showExceptionMessage(String message) {
        MyLog.e(message);
        final AlertDialog a = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    protected void showMessage(String message) {
        MyLog.i(message);
        final AlertDialog a = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }
    //endregion
}
